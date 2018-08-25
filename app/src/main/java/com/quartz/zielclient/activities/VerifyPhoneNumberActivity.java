package com.quartz.zielclient.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.quartz.zielclient.R;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.view.View.OnClickListener;

public class VerifyPhoneNumberActivity extends AppCompatActivity implements OnClickListener {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);

    // Check if we have access to the phone number.
    String phoneNumber = "";
    String isoCode = "";
    if (checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
      TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
      if (telephonyManager != null) {
        phoneNumber = telephonyManager.getLine1Number();
        isoCode = telephonyManager.getSimCountryIso();
      }
    }

    TextView phoneNumberEntry = findViewById(R.id.phoneEntry);
    phoneNumberEntry.setText(phoneNumber);

    TextView isoEntry = findViewById(R.id.isoEntry);
    isoEntry.setText(isoFormat(isoCode));

    setContentView(R.layout.activity_verify_phone_number);
  }

  @Override
  public void onClick(View view) {
    final int clickedId = view.getId();
    if (clickedId != R.id.confirmNumber) {
      // We only want to handle this for confirmations.
      return;
    }


  }

  private static String isoFormat(String isoCode) {
    return "+" + isoCode;
  }
}
