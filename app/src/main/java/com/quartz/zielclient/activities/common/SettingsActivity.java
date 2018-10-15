package com.quartz.zielclient.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.assisted.AssistedHomePageActivity;
import com.quartz.zielclient.activities.carer.CarerHomepageActivity;
import com.quartz.zielclient.user.User;
import com.quartz.zielclient.user.UserFactory;

public class SettingsActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

  private boolean cancellingChanges = false;
  private boolean cancelledChanges = false;
  private boolean updating = false;

  private User user;
  private User initialUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    setContentView(R.layout.settings_activity);

    Bundle userBundle = getIntent().getBundleExtra("user");
    User thisUser = UserFactory.getUser(userBundle);
    user = thisUser;
    initialUser = thisUser;
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
        confirmChanges();
        break;
      case R.id.settingsCancel:
        cancel();
        break;
      default:
        break;
    }
  }

  private void confirmChanges() {
    // todo update database
  }

  /**
   * Ignore changes, and go back to the home screen.
   */
  private void cancel() {
    if (updating) {
      cancelledChanges = true;
      Toast.makeText(this, "Cancelled changes", Toast.LENGTH_SHORT).show();
    }

    goHome();
  }

  /**
   * Go back to the appropriate home page.
   */
  private void goHome() {
    Class<? extends AppCompatActivity> homePage = user.isAssisted()
        ? AssistedHomePageActivity.class
        : CarerHomepageActivity.class;
    Intent intent = new Intent(this, homePage);
    startActivity(intent);
    finish();
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    if (cancelledChanges) {
      // todo set user back to initialuser
      cancelledChanges = false;
      cancellingChanges = true;
      updating = false;
    } else if (cancellingChanges) {
      user = UserFactory.getUser(dataSnapshot);
      cancellingChanges = false;
      goHome();
    } else {
      user = UserFactory.getUser(dataSnapshot);
      updating = false;
      populateUi();
    }
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    // TODO
  }
}