package com.quartz.zielclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.common.LaunchPadActivity;
import com.quartz.zielclient.activities.signup.SignUpActivity;
import com.quartz.zielclient.user.UserController;

public class SplashScreenActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = new Intent();
    if (UserController.isSignedIn()) {
      intent.setClass(this, LaunchPadActivity.class);
    } else {
      intent.setClass(this, SignUpActivity.class);
    }

    Handler handler = new Handler();
    Runnable r = () -> {
      startActivity(intent);
      finish();
    };

    // Setting the timer on the splash screen.
    handler.postDelayed(r, 1500);
  }
}