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

  /**
   * create and initialise a channel
   *
   * @param channelListener Callback that listens on the channel's database entry.
   * @return A new channel.
   */
  public static ChannelData createChannel(ChannelListener channelListener) {
    Log.i("ChannelController", "Creating new channel");
    final String channelKey = UUID.randomUUID().toString();
    return new ChannelData(channelsReference.child(channelKey), channelListener);
  }

  /**
   * Use this if you dont want to create a channel but you want to retrieve an already created channel
   *
   * @param channelId       ID of the channel.
   * @param channelListener Callback that listens on the channel's database entry.
   * @return The existing channel
   */
  public static ChannelData retrieveChannel(String channelId, ChannelListener channelListener) {
    return new ChannelData(channelsReference.child(channelId), channelListener);
  }
}