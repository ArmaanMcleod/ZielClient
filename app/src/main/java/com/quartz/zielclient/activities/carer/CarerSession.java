package com.quartz.zielclient.activities.carer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.utilities.channel.Channel;
import com.quartz.zielclient.utilities.channel.ChannelHandler;
import com.quartz.zielclient.utilities.channel.ChannelListener;
import com.quartz.zielclient.utilities.carer.NotificationHandler;

import static android.view.View.VISIBLE;

/**
 * This activity allows the Carer to accept a session with an assisted and allows them to establish
 * redimentary communication.
 *
 * @author Bilal Shehata
 */
public class CarerSession extends AppCompatActivity implements
    ValueEventListener, View.OnClickListener, ChannelListener {

  private TextView status;
  private FirebaseDatabase firebaseDatabase;
  private DatabaseReference channelReference;
  private Button acceptButton;
  // initialise Notification manager will allow push-notifcations on the device
  private NotificationHandler notificationHandler;
  // temporary id value for the carer until authorization is complete
  private final String id = "carer1";
  private Channel channel;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // create a notification handler to provide notifications to the carer
    notificationHandler = new NotificationHandler(getApplicationContext());
    notificationHandler.createNotificationChannel();
    // set listener to watch database incase someone needs assistance
    watchNotificationChange();
    // set the content for the layout
    setContentView(R.layout.acitivty_carer_session);
    // bind graphical buttons to functional buttons
    acceptButton = findViewById(R.id.acceptButton);
    // do not make the accept button visible unless an assisted has requested assistance
    acceptButton.setVisibility(View.INVISIBLE);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    status = findViewById(R.id.channelStatus);
    acceptButton.setOnClickListener(this);
  }

  @Override
  public void onBackPressed() {
    if (channelReference != null) {
      channelReference.child("carerStatus").setValue(false);
    }
    super.onBackPressed();
  }


  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    // Get the session which has been allocated to the carer from an assisted
    String channelID = dataSnapshot.child(getResources().getString(R.string.current_channel)).getValue(String.class);

    // get a reference to the session that was created by the assisted
    channelReference = firebaseDatabase.getReference(getString(R.string.channelsReferenceLocation) + channelID);

    // incase of misfire ensure that Id returned a string value
    if (channelID != null && !channelID.equals(getResources().getString(R.string.waiting))) {
      // Send a toast to the user notifying them of the request
      Toast.makeText(getApplicationContext(),
          "request has been made", Toast.LENGTH_LONG).show();
      // create the notfication for the user
      notificationHandler.notifyUserToOpenApp();
      // allow the accept button to appear now that there is a session to accept
      acceptButton.setVisibility(VISIBLE);
      // begin listening to the session
      channel = ChannelHandler.retrieveChannel(channelID, this);
    }
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    //TODO
  }

  @Override
  public void onClick(View view) {
    channelReference.child(getString(R.string.carer_status)).setValue(true);
  }

  private void watchNotificationChange() {
    firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference notifcationRef = firebaseDatabase.getReference("users/" + id);

    notifcationRef.child(getResources()
        .getString(R.string.current_channel))
        .setValue(getResources().getString(R.string.waiting));

    notifcationRef.addValueEventListener(this);
  }

  /**
   * notification that the data has changed on the channel
   */
  @Override
  public void dataChanged() {
    // check values on channel and modify state accordingly
    if (channel.getAssistedStatus()) {
      status.setText(R.string.channel_active);
    }
    if (!channel.getAssistedStatus()) {
      status.setText(R.string.channel_inactive);
    }
    if (channel.getPing()) {
      channel.setPing(false);
      Toast.makeText(
          getApplicationContext(),
          "Assisted has waved",
          Toast.LENGTH_LONG
      ).show();
    }

  }

  @Override
  public String getAssistedId() {
    return null;
  }

  @Override
  public String getCarerId() {
    return id;
  }
}