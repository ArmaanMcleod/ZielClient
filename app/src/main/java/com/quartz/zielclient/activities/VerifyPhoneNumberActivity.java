package com.quartz.zielclient.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quartz.zielclient.R;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.view.View.OnClickListener;

public class VerifyPhoneNumberActivity extends AppCompatActivity implements OnClickListener {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verify_phone_number);

    requestPermissions(new String[]{READ_PHONE_STATE}, 1);
    populatePhoneNumber();

    Button confirmButton = findViewById(R.id.confirmNumber);
    confirmButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    final int clickedId = view.getId();
    if (clickedId != R.id.confirmNumber) {
      // We only want to handle this for confirmations.
      return;
    }


  }

  /**
   * Attempts to access the phone number, and then set it in the text box. If it's unable (due to
   * lacking permissions or some other problem) it will simply set the text box to empty, allowing
   * the user to enter their own phone number.
   */
  private void populatePhoneNumber() {
    String phoneNumber = "";
    if (checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
      TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
      if (telephonyManager != null) {
        phoneNumber = telephonyManager.getLine1Number();
      }
    }

    TextView phoneNumberEntry = findViewById(R.id.phoneEntry);
    phoneNumberEntry.setText(phoneNumber);
  }
}
