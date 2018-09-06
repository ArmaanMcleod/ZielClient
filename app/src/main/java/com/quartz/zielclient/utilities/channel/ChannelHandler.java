package com.quartz.zielclient.utilities.channel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Session handler object handlers the creation of sessions and notifying the relevant users
 * that a session has been created for them.
 *
 * @author Bilal Shehata
 */
public final class ChannelHandler {

  private ChannelHandler() {
  }

  private static FirebaseDatabase firebaseDatabase;
  private static DatabaseReference channelsReference;
  private static DatabaseReference usersReference;

  // Initialize reference into the firebase DB.
  static {
    staticInit();
  }

  /**
   * create and initialise a channel
   *
   * @param channelListener Callback that listens on the channel's database entry.
   * @return A new channel.
   */
  public static Channel createChannel(ChannelListener channelListener) {
    final String channelKey = UUID.randomUUID().toString();
    Channel channel = new Channel(channelsReference.child(channelKey), channelListener);
    // initialize coordinates in database
    channel.setAssistedLocation(0,0);
    // add the id of the assisted to the session
    channel.setAssisted(channelListener.getAssistedId());
    // add the id of the carer to the session
    channel.setCarer(channelListener.getCarerId());
    // the assisted created the session therefore they must currently be active
    channel.setAssistedStatus(true);
    // the carer is inactive since they still need to accept the session
    channel.setCarerStatus(false);
    // the Ping feature will begin as inactive indicating a wave has not occured.
    channel.setPing(false);
    // update and notify the user
    usersReference.child(channelListener.getCarerId()).child("currentSession").setValue(channelKey);
    usersReference.child(channelListener.getCarerId()).child("status").setValue("notified");
    return channel;
  }


  /**
   * Use this if you dont want to create a channel but you want to retrieve an already created channel
   *
   * @param channelID       ID of the channel.
   * @param channelListener Callback that listens on the channel's database entry.
   * @return The existing channel
   */
  public static Channel retrieveChannel(String channelID, ChannelListener channelListener) {
    return new Channel(channelsReference.child(channelID), channelListener);
  }

  public static void staticInit() {
    firebaseDatabase = FirebaseDatabase.getInstance();
    // location of sessions in database
    channelsReference = firebaseDatabase.getReference("channels/");
    // location of users in the database
    usersReference = firebaseDatabase.getReference("users/");
  }
}
