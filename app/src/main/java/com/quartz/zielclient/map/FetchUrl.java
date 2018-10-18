package com.quartz.zielclient.map;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is responsible for fetching the path JSON data from the API endpoint.
 *
 * @author Armaan McLeod
 * @version 1.0- 1
 * 01/09/2018
 */
public class FetchUrl extends AsyncTask<String, Void, String> {

  private static final String ACTIVITY = "FetchUrl";

  private final GoogleMap googleMap;

  private HTTP http;

  public FetchUrl(GoogleMap googleMap) {
    super();
    this.googleMap = googleMap;
    http = new HTTP();
  }

  /**
   * This downloads the specified URL in a background thread.
   * <p>
   * Documentation : https://developer.android.com/reference/android/os/
   * AsyncTask.html#doInBackground(Params...)
   *
   * @param url This is the URL to download.
   * @return String This is the downloaded data in String format.
   */
  @Override
  protected String doInBackground(@NonNull String... url) {
    String data = "";

    // Attempt to download the URL
    try {
      data = http.downloadUrl(url[0]);
      Log.d(ACTIVITY, data);
    } catch (Exception e) {
      Log.d(ACTIVITY, e.toString());
    }

    return data;
  }

  /**
   * This is responsible for running the parser task on the
   * download URL from doInBackground(Params...)
   * <p>
   * Documentation : https://developer.android.com/reference/android/os/
   * AsyncTask.html#onPostExecute(Result)
   *
   * @param result This is the result to execute.
   */
  @Override
  protected void onPostExecute(@NonNull String result) {
    super.onPostExecute(result);

    ParserTask parserTask = new ParserTask(googleMap);
    parserTask.execute(result);
  }

}
