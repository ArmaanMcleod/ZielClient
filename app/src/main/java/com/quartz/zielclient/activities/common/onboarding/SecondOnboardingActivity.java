package com.quartz.zielclient.activities.common.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.signup.SignUpActivity;

public class SecondOnboardingActivity extends AppCompatActivity implements View.OnClickListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_help_onboarding_page2);

    Button nextButton = findViewById(R.id.next2);
    nextButton.setOnClickListener(this);

    Button skipButton = findViewById(R.id.signup2);
    skipButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.next2) {
      startActivity(new Intent(this, FinalOnboardingActivity.class));
    } else if (v.getId() == R.id.signup2) {
      startActivity(new Intent(this, SignUpActivity.class));
    }
  }
}
