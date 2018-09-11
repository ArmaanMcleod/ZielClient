package com.quartz.zielclient.activities.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.HomePageActivity;


/**
 * This activity is a temporary showcase of the features of the application
 * it acts as a navigation board to explore features and allow for continous integretion
 * <p>
 * THis activity will not be present in the final product.
 *
 * @author Bilal Shehata
 */
public class LaunchPadActivity extends AppCompatActivity implements View.OnClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launch_pad);
    Button signIn = findViewById(R.id.signInButton);
    Button signUp = findViewById(R.id.signUpButton);
    Button navigation = findViewById(R.id.navigationButton);
    Button textChat = findViewById(R.id.textChatButton);
    Button sessionMaker = findViewById(R.id.sessionMakerButton);
    Button assistedHome = findViewById(R.id.assistedHome);
    signIn.setOnClickListener(this);
    signUp.setOnClickListener(this);
    navigation.setOnClickListener(this);
    textChat.setOnClickListener(this);
    sessionMaker.setOnClickListener(this);
    assistedHome.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.signInButton:
        // do your code
        break;
      case R.id.signUpButton:
        // do your code
        break;
      case R.id.textChatButton:
        startActivity(new Intent(LaunchPadActivity.this,TextChatActivity.class));
        break;
      case R.id.navigationButton:
        startActivity(new Intent(LaunchPadActivity.this, MapsActivity.class));
        break;
      case R.id.sessionMakerButton:
        startActivity(new Intent(LaunchPadActivity.this, ManualRedirect.class));
        break;
      case R.id.assistedHome:
        startActivity(new Intent(LaunchPadActivity.this, HomePageActivity.class));
        break;
      default:
        break;
    }
  }
}
