package com.quartz.zielclient.utilities.map;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for extracting information from JSON responses into paths.
 *
 * @author Armaan McLeod
 * @version 1.0- 1
 * 01/09/2018
 */
public class ParserTask extends AsyncTask<String, Integer, List<List<Map<String, String>>>> {

  private final String TAG = this.getClass().getSimpleName();

  private static int POLYLINE_WIDTH = 16;

  private static final String POLYLINE_COLOR = "#2196F3";

  private GoogleMap googleMap;

  public ParserTask(GoogleMap googleMap) {
    this.googleMap = googleMap;
  }

  /**
   * This does the parsing in a background thread.
   *
   * Documentation : https://developer.android.com/reference/android/os/
   * AsyncTask.html#doInBackground(Params...)
   *
   * @param jsonData This is the JSON data to parse.
   * @return List<List<Map<String, String>>> The routes extracted from JSON data.
   */
  @Override
  protected List<List<Map<String, String>>> doInBackground(@NonNull String... jsonData) {
    List<List<Map<String, String>>> routes = Collections.emptyList();

    // If possible, extract routes from JSON object
    try {
      JSONObject jObject = new JSONObject(jsonData[0]);
      Log.d(TAG, jsonData[0]);

      DirectionsJSONParser parser = new DirectionsJSONParser();
      Log.d(TAG, parser.toString());

      routes = parser.parse(jObject);
      Log.d(TAG, "Executing routes");
      Log.d(TAG, routes.toString());

    } catch (Exception e) {
      Log.d(TAG, e.toString());
    }

    return routes;
  }

  /**
   * Decodes line options into a visible path on the map.
   *
   * Documentation : https://developer.android.com/reference/android/os/
   * AsyncTask.html#onPostExecute(Result)
   *
   * @param result This is result from doInBackground(Params...).
   */
  @Override
  protected void onPostExecute(@NonNull List<List<Map<String, String>>> result) {
    PolylineOptions lineOptions = null;

    for (List<Map<String, String>> path : result) {
      List<LatLng> points = new ArrayList<>();
      lineOptions = new PolylineOptions();

      for (Map<String, String> point : path) {
        double latitude = Double.parseDouble(point.get("lat"));
        double longitude = Double.parseDouble(point.get("lng"));

        LatLng position = new LatLng(latitude, longitude);
        points.add(position);
      }

      // Add points and colour line
      lineOptions.addAll(points);
      lineOptions.width(POLYLINE_WIDTH);
      lineOptions.color(Color.parseColor(POLYLINE_COLOR)).geodesic(true).zIndex(8);

      Log.d(TAG, "lineOptions decoded");
    }

    // Ensure polylines available to draw on screen
    if (lineOptions != null) {
      googleMap.addPolyline(lineOptions);
    } else {
      Log.d(TAG, "No polylines drawn");
    }
  }
}
