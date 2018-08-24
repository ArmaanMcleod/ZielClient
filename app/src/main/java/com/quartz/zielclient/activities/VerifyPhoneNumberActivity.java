package com.quartz.zielclient.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.quartz.zielclient.R;

import java.util.Optional;

public class VerifyPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener {

  private String phoneNum;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    Optional<String> phoneNumber = Optional.ofNullable(getIntent().getStringExtra("phoneNum"));
    if (phoneNumber.isPresent()) {
      this.phoneNum = phoneNumber.get();
    } else {
      // todo error handling
    }

    setContentView(R.layout.activity_signup);
  }

  @Override
  public void onClick(View view) {

  }
}
