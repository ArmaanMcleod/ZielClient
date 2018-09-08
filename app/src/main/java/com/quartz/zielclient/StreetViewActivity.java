package com.quartz.zielclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;

public class StreetViewActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_street_view);
  }
}
