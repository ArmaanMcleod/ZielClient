package com.quartz.zielclient.channel;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Session handler object handlers the creation of sessions and notifying the relevant users
 * that a session has been created for them.
 *
 * @author Bilal Shehata
 */
public final class ChannelController {

  private ChannelController() {
  }

  private static DatabaseReference channelsReference = FirebaseDatabase.getInstance().getReference("channels");
  private static DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");

  /**
   * create and initialise a channel
   *
   * @param channelListener Callback that listens on the channel's database entry.
   * @return A new channel.
   */
  public static ChannelData createChannel(ChannelListener channelListener) {
    Log.i("ChannelController", "Creating new channel");
    final String channelKey = UUID.randomUUID().toString();
    ChannelData channelData = new ChannelData(channelsReference.child(channelKey), channelListener);
    // add the id of the assisted to the session
    channelData.setAssisted(channelListener.getAssistedId());
    // add the id of the carer to the session
    channelData.setCarer(channelListener.getCarerId());
    // the assisted created the session therefore they must currently be active
    channelData.setAssistedStatus(true);
    // the carer is inactive since they still need to accept the session
    channelData.setCarerStatus(false);
    // the Ping feature will begin as inactive indicating a wave has not occured.
    channelData.setPing(false);
    // update and notify the user
    usersReference.child(channelListener.getCarerId()).child("currentSession").setValue(channelKey);
    usersReference.child(channelListener.getCarerId()).child("status").setValue("notified");
    return channelData;
  }

  /**
   * Use this if you dont want to create a channel but you want to retrieve an already created channel
   *
   * @param channelID       ID of the channel.
   * @param channelListener Callback that listens on the channel's database entry.
   * @return The existing channel
   */
  public static ChannelData retrieveChannel(String channelID, ChannelListener channelListener) {
    return new ChannelData(channelsReference.child(channelID), channelListener);
  }
}