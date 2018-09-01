package com.quartz.zielclient.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.common.LaunchPadActivity;

public class SplashScreenActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    // Creating Handler to run screen activity.
    Handler handler = new Handler();
    Runnable r = () -> {
      Intent intent = new Intent(SplashScreenActivity.this, LaunchPadActivity.class);
      startActivity(intent);
      finish();
    };

    // Setting the timer on the splash screen.
    handler.postDelayed(r, 3000);
  }
}