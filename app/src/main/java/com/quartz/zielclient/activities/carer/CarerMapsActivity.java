package com.quartz.zielclient.activities.carer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.utilities.channel.Channel;
import com.quartz.zielclient.utilities.channel.ChannelHandler;
import com.quartz.zielclient.utilities.channel.ChannelListener;
import com.quartz.zielclient.utilities.map.FetchUrl;

public class CarerMapsActivity extends AppCompatActivity implements OnMapReadyCallback, ChannelListener {

  private GoogleMap mGoogleMap;


  final Double[] latitu = {-37.7964};
  final Double[] longitu = {144.9612};
  MarkerOptions mop = new MarkerOptions();
  Marker assistedMarker;
  // test channel
  Channel channel  = ChannelHandler.retrieveChannel("90a2c51d-4d9a-4d15-af8e-9639ff472231",this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_carer_maps);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

  }


  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    mop.position(new LatLng(0,0));
    mop.title("Assisted Location");

    assistedMarker  = mGoogleMap.addMarker(mop);
    updateMapCoords();


  }

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

  @Override
  public void dataChanged() {
    latitu[0] = channel.getAssistedLocation().latitude;
    longitu[0] = channel.getAssistedLocation().longitude;
    updateMapCoords();
    if((channel.getDirectionsURL() != null) && !channel.getDirectionsURL().equals("none")){
      FetchUrl fetchUrl = new FetchUrl(mGoogleMap);
      fetchUrl.execute(channel.getDirectionsURL());
    }

  }

  @Override
  public String getAssistedId() {
    return "Assisted1";
  }

  @Override
  public String getCarerId() {
    return "carer1";
  }
}

