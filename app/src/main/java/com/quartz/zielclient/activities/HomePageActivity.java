package com.quartz.zielclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.controllers.UserController;
import com.quartz.zielclient.exceptions.UserNotFoundException;
import com.quartz.zielclient.models.User;

public class HomePageActivity extends Activity {

  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_page);

    try {
      user = UserController.fetchUser(getIntent().getStringExtra("UID"));
    } catch (UserNotFoundException e) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    }

    TextView userText = findViewById(R.id.tempUserView);
    userText.setText(user.toString());
  }
}
