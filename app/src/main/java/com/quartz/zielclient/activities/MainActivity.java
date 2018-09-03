package com.quartz.zielclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.signup.SignUpActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
    startActivity(intent);
  }
}
