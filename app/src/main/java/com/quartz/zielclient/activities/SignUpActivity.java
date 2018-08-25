package com.quartz.zielclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quartz.zielclient.R;

import static android.Manifest.permission.READ_PHONE_STATE;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
  private static final int SUCCESS = 1;

  private Button button;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    button = findViewById(R.id.signup);
    button.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    int clickedId = view.getId();
    if (clickedId != R.id.signup) {
      return;
    }

    requestPermissions(new String[]{READ_PHONE_STATE}, SUCCESS);
    Intent intent = new Intent(this, VerifyPhoneNumberActivity.class);
    startActivity(intent);
    finish();
  }
}