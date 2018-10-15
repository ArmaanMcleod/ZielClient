package com.quartz.zielclient.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.assisted.AssistedHomePageActivity;
import com.quartz.zielclient.activities.carer.CarerHomepageActivity;
import com.quartz.zielclient.user.User;
import com.quartz.zielclient.user.UserController;
import com.quartz.zielclient.user.UserFactory;

public class SettingsActivity extends AppCompatActivity
    implements ValueEventListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

  private boolean cancellingChanges = false;
  private boolean cancelledChanges = false;
  private boolean updating = false;

  // Current user state
  private User user;

  // User when they first opened the page
  private User initialUser;

  private TextView firstNameEntry;
  private TextView lastNameEntry;
  private Switch roleSwitch;

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
//      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  /**
   * Populate the UI based on the current user model.
   */
  private void populateUi() {
    firstNameEntry = findViewById(R.id.settingFirstName);
    firstNameEntry.setText(user.getFirstName());

    lastNameEntry = findViewById(R.id.settingLastName);
    lastNameEntry.setText(user.getLastName());

    String roleString = user.isAssisted() ? "Assisted" : "Carer";
    roleSwitch = findViewById(R.id.roleSwitch);
    roleSwitch.setChecked(user.isAssisted());
    roleSwitch.setOnCheckedChangeListener(this);
    roleSwitch.setText(roleString);

    Button confirmButton = findViewById(R.id.settingsConfirm);
    confirmButton.setOnClickListener(this);

    Button cancelButton = findViewById(R.id.settingsCancel);
    cancelButton.setOnClickListener(this);
  }

//  @Override
//  public boolean onSupportNavigateUp() {
//    Intent intent = new Intent();
//    if (updating) {
//      return false;
//    }
//
//    if (user.isAssisted())
//    //code it to launch an intent to the activity you want
//    finish();
//    return true;
//  }

  /**
   * Updates the database with the current UI values.
   */
  private void confirmChanges() {
    updating = true;
    String firstName = firstNameEntry.getText().toString();
    String lastName = lastNameEntry.getText().toString();
    boolean isAssisted = roleSwitch.isChecked();

    User updatedUser = UserFactory.getUser(firstName, lastName, user.getPhoneNumber(), isAssisted);
    UserController.updateSelf(updatedUser, this);
  }

  /**
   * Ignore changes, and go back to the home screen.
   */
  private void cancel() {
    if (updating) {
      cancelledChanges = true;
      Toast.makeText(this, "Cancelled changes.", Toast.LENGTH_SHORT).show();
    } else if (!cancelledChanges && !cancellingChanges) {
      goHome();
    }
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

  /**
   * Updates the text next to the role switch.
   * @param buttonView The role switch view.
   * @param isChecked Whether or not the switch has been checked.
   */
  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (isChecked) {
      buttonView.setText("Assisted");
    } else {
      buttonView.setText("Carer");
    }
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    if (cancelledChanges) {
      // todo set user back to initialuser
      cancelledChanges = false;
      cancellingChanges = true;
      updating = false;
      UserController.updateSelf(initialUser, this);
    } else if (cancellingChanges) {
      user = UserFactory.getUser(dataSnapshot);
      cancellingChanges = false;
      goHome();
    } else {
      user = UserFactory.getUser(dataSnapshot);
      updating = false;
      populateUi();
      Toast.makeText(this, "Updated account settings.", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    // TODO
  }
}