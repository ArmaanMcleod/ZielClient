package com.quartz.zielclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.common.MapsActivity;

import java.util.Objects;

public class HomePageActivity extends Activity {

  private LatLng destination;

  private final String activity = this.getClass().getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.HomeTheme);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_page);

    // Create autocomplete bar
    PlaceAutocompleteFragment placeAutoComplete = (PlaceAutocompleteFragment)
        getFragmentManager().findFragmentById(R.id.editText);
    Objects.requireNonNull(placeAutoComplete.getView()).setBackgroundColor(Color.WHITE);
    placeAutoComplete.setHint("Search Place");

    placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(Place place) {
        Log.d(activity, "Place selected: " + place.getLatLng());
        destination = place.getLatLng();
      }

      @Override
      public void onError(Status status) {
        Log.d(activity, "An error occurred: " + status);
      }
    });

    Button directMeButton = findViewById(R.id.directMeButton);
    directMeButton.setOnClickListener(v -> {
      if (destination != null) {
        Intent intent = new Intent(HomePageActivity.this, MapsActivity.class);
        intent.putExtra("destination", destination);
        startActivity(intent);
      } else {
        Toast.makeText(this,
            "Please select a place before proceeding",
            Toast.LENGTH_LONG).show();
      }
    });
  }
}