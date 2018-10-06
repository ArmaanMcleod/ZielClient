package com.quartz.zielclient.activities.common.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.quartz.zielclient.R;

public class OnBoardingActivity extends AppCompatActivity implements View.OnClickListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_help_onboarding_page1);


  }

  @Override
  public void onClick(View v) {
  }
}
