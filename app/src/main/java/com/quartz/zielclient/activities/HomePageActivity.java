package com.quartz.zielclient.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.quartz.zielclient.R;

import java.util.Objects;

public class HomePageActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.HomeTheme);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_page);

    // Create autocomplete bar
    PlaceAutocompleteFragment placeAutoComplete = (PlaceAutocompleteFragment)
        getFragmentManager().findFragmentById(R.id.editText);
    Objects.requireNonNull(placeAutoComplete.getView()).setBackgroundColor(Color.WHITE);
    placeAutoComplete.setHint("Where To");

    // TODO: Add listener here for autocomplete bar and pass to MapsActivity
  }
}