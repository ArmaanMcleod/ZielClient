package com.quartz.zielclient.activities.assisted;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.quartz.zielclient.R;
import com.quartz.zielclient.utilities.channel.Channel;
import com.quartz.zielclient.utilities.channel.ChannelHandler;
import com.quartz.zielclient.utilities.channel.ChannelListener;


/**
 * Assisted Session maintains and provides a session to the user from the Assisteds Perspective
 * This activity allows the user to ask for assistance from a carer and share personal data
 * with the carer to allow them to provide assistance
 *
 * @author Bilal Shehata
 */
public class AssistedSession extends AppCompatActivity implements ChannelListener, View.OnClickListener {
  // common objects within the activity
  //display the users status
  private TextView status;
  //temporary variables to store a users role -> this will be replaced by values from the user roles in the database
  private String myId = "assisted1";
  //store reference to the database where the session exists
  private Channel channel;
  private DatabaseReference channelListener;
  //button allows the assisted to wave to carer (send a toast)
  private Button waveButton;
  private String myCarerId = "carer1";

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // set the content view to the assisted_session
    setContentView(R.layout.activity_assisted_session);
    // allocate the graphical button to a functional button
    waveButton = findViewById(R.id.waveButton);
    // button should not be visible until a session is established.
    waveButton.setVisibility(View.INVISIBLE);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    status = findViewById(R.id.channelStatus);
    channel = ChannelHandler.createChannel(this);
  }

  @Override
  public void dataChanged() {
    // sometimes the listener can misfire so avoid crash by checking an object was collected.
    if (channel.getCarerStatus()) {
      //the Carer set their status to true they are active
      status.setText("Session is active");
      waveButton.setVisibility(View.VISIBLE);
      waveButton.setOnClickListener(this);
    }
    if (!channel.getCarerStatus()) {
      // the Carer has set or has not unset their status and therefore they are inactive.
      status.setText("Session is Inactive");
    }

  }

  @Override
  public void onBackPressed() {
    // assisted has exited the session so set status to false (inactive)
    channel.setAssistedStatus(false);
    super.onBackPressed();
  }

  @Override
  public void onClick(View view) {
    channel.setPing(true);
  }

  @Override
  public String getAssistedId() {
    return myId;
  }

  @Override
  public String getCarerId() {
    return myCarerId;
  }


}


