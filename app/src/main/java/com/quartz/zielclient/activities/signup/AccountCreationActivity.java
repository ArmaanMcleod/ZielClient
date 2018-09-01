package com.quartz.zielclient.activities.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.SplashScreenActivity;

import static android.view.View.OnClickListener;
import static android.widget.RadioGroup.OnCheckedChangeListener;

public class AccountCreationActivity extends AppCompatActivity implements OnClickListener, OnCheckedChangeListener {

  private RadioGroup carerOrAssistedGroup;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Button accountCreationButton = findViewById(R.id.completeAccountCreation);
    accountCreationButton.setOnClickListener(this);

    // Initialise radio buttons group
    carerOrAssistedGroup = findViewById(R.id.carerOrAssistedGroup);
    carerOrAssistedGroup.setOnCheckedChangeListener(this);
  }

  @Override
  public void onClick(View v) {
    // We only want to listen on the account creation button
    if (v.getId() != R.id.completeAccountCreation) {
      return;
    }

//    try {
//      FirebaseUser firebaseUser = UserController.retrieveFirebaseUser();
//      UserController.createUser(firebaseUser);
//
//    } catch (AuthorisationException e) {
//      handleLoginFailure();
//    }

  }

  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    switch (checkedId) {
      case R.id.assistedChoice:
        break;
      case R.id.carerChoice:
        break;
      default:
        break;
    }
  }

  private void handleLoginFailure() {
    Toast toast = Toast.makeText(this, R.string.failed_creation, Toast.LENGTH_SHORT);
    toast.show();

    Intent intent = new Intent(this, SplashScreenActivity.class);
    startActivity(intent);
    finish();
  }
}