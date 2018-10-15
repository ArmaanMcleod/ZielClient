package com.quartz.zielclient.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.user.User;
import com.quartz.zielclient.user.UserFactory;

public class SettingsActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

  private boolean updating = false;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    setContentView(R.layout.settings_activity);

    Bundle userBundle = getIntent().getBundleExtra("user");
    user = UserFactory.getUser(userBundle);
    populateUi();

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle("Account Settings");
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  private void populateUi() {
    // todo display appropriate change role button
    if (user.isAssisted()) {
    } else {
    }

    // todo populate textviews etc
  }

  @Override
  public boolean onSupportNavigateUp() {
//    Intent intent = new Intent();
//    if (updating) {
//      return false;
//    }
//
//    if (user.isAssisted())
    //code it to launch an intent to the activity you want
    finish();
    return true;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.settingsConfirm:
      case R.id.settingsCancel:
      default:
        break;
    }
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    user = UserFactory.getUser(dataSnapshot);
    updating = false;
    populateUi();
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    // TODO
  }
}