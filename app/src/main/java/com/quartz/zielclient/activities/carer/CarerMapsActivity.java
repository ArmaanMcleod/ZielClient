package com.quartz.zielclient.activities.carer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quartz.zielclient.R;
import com.quartz.zielclient.utilities.channel.Channel;
import com.quartz.zielclient.utilities.channel.ChannelHandler;
import com.quartz.zielclient.utilities.channel.ChannelListener;
import com.quartz.zielclient.utilities.map.FetchUrl;

/**
 * This activity Reads coordinate and route information from a channel and displays a
 * Map accordigly THis allows a carer to have upto date locative information from the assisted
 * @author Bilal Shehata
 */
public class CarerMapsActivity extends AppCompatActivity implements OnMapReadyCallback, ChannelListener {

  private GoogleMap mGoogleMap;
  // These constants are displayed until map syncronizes (only momentarily)
  // This prevents the default usage of  0,0
  private double MELBOURNEUNILAT = -37.7964;
  private double MELBOURNEUNILONG = 144.9612;
  // default to melbourne uni
  // list of Assisted movements
  final Double[] latitu = {MELBOURNEUNILAT};
  final Double[] longitu = {MELBOURNEUNILONG};
  // initialize assisted location marker
  MarkerOptions assistedMarkerOptions = new MarkerOptions();
  Marker assistedMarker;
  // test channel to be replaced with the current channel that was handled by a previous activity.
  Channel channel  = ChannelHandler.retrieveChannel("90a2c51d-4d9a-4d15-af8e-9639ff472231",this);

  /**
   * Initizialise the activity
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // set xml view file
    setContentView(R.layout.activity_carer_maps);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

  }
  /**
   *  Manipulates the map once available.
   *  Once map is ready add a temporary marker
   *  (once again The University of Melbourne is used temporarily  rather than the default NUll-Island)
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    // place a marker on melbourne uni whilst synchronization occurs
    assistedMarkerOptions.position(new LatLng(MELBOURNEUNILAT,MELBOURNEUNILONG));
    assistedMarkerOptions.title("Assisted Location");
    assistedMarker  = mGoogleMap.addMarker(assistedMarkerOptions);
    // update the coordinates from the channel
    updateMapCoords();
  }

  /**
   *  Update the Coordinates based on the latest Assisted's location
   */
  public void updateMapCoords(){
    LatLng assistedLocation = new LatLng(latitu[0], longitu[0]);
    assistedMarker.setPosition(assistedLocation);
    mGoogleMap
            .moveCamera(CameraUpdateFactory
                    .newLatLng(assistedLocation));
    mGoogleMap
            .animateCamera(CameraUpdateFactory
                    .newLatLngZoom(assistedLocation, 13f));
  }

  /**
   *  THis listens to changes in the channel
   *  Once a change occurs update the long/lat values
   */
  @Override
  public void dataChanged() {
    // update latitude
    latitu[0] = channel.getAssistedLocation().latitude;
    // update longtidute
    longitu[0] = channel.getAssistedLocation().longitude;
    // update on map marker
    updateMapCoords();
    // if the assisted has entered a route then generate that same route
    if((channel.getDirectionsURL() != null) && !channel.getDirectionsURL().equals("none")){
      FetchUrl fetchUrl = new FetchUrl(mGoogleMap);
      fetchUrl.execute(channel.getDirectionsURL());
    }
  }

 // return the carer and assisted associated to this activtiy
 // should become getUser.Id and getAssisted.ID
  @Override
  public String getAssistedId() {
    return "Assisted1";
  }

  @Override
  public String getCarerId() {
    return "carer1";
  }
}

