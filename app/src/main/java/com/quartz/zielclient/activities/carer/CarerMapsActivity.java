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

public class CarerMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

  private GoogleMap mMap;

  final Double[] latitu = {7.02343187};
  final Double[] longitu = {79.89658312};
  MarkerOptions mop = new MarkerOptions();
  Marker assistedMarker;

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
    mMap = googleMap;
    mop.position(new LatLng(0,0));
    mop.title("Assisted Location");

    assistedMarker  = mMap.addMarker(mop);
    updateMapCoords();


   DatabaseReference myRef = FirebaseDatabase.getInstance()
            .getReference().child("channels").child("90a2c51d-4d9a-4d15-af8e-9639ff472231").child("assistedLocation");
    myRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot
                                       dataSnapshot) {
        Log.d("UPDATEHAPPENED","DATA SNAPSHOT UPDATED");
        latitu[0] = Double.valueOf(String.valueOf(dataSnapshot.child("xCoord").getValue()));
        Log.d("UPDATEHAPPENED",Double.valueOf(String.valueOf(dataSnapshot.child("xCoord").getValue())).toString());
        Log.d("UPDATEHAPPENED",latitu[0].toString());
        longitu[0] = Double.valueOf(String.valueOf(dataSnapshot.child("yCoord").getValue()));
        updateMapCoords();


      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });


  }

  public void updateMapCoords(){
    LatLng assistedLocation = new LatLng(latitu[0], longitu[0]);


    assistedMarker.setPosition(assistedLocation);
    mMap
            .moveCamera(CameraUpdateFactory
                    .newLatLng(assistedLocation));
    mMap
            .animateCamera(CameraUpdateFactory
                    .newLatLngZoom(assistedLocation, 13f));


  }
}

