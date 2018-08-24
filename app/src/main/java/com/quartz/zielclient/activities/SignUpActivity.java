package com.quartz.zielclient.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.quartz.zielclient.R;

import static android.Manifest.permission.READ_PHONE_STATE;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
  private Button button;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
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

    // Check if we do not have access to the phone number
    if (checkSelfPermission(READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
    }

    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    String phoneNumber = telephonyManager.getLine1Number();

    Intent intent = new Intent(this, VerifyPhoneNumberActivity.class);
    intent.putExtra("phoneNumber", phoneNumber);
    startActivity(intent);
    finish();
  }
}