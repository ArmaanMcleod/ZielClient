package com.quartz.zielclient.utilities.channel;

/**
 * interface for objects that wish to listen to a channel
 *
 * @author Bilal Shehata
 */
public interface ChannelListener {
  /**
   * object needs to recieve updatea when values in the channel have been updated
   */
  public void dataChanged();

  public String getAssistedId();

  public String getCarerId();

}