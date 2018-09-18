package com.quartz.zielclient.models;

import android.os.Bundle;

public class ChannelRequest implements Model {

  private static final String NAME_KEY = "name";
  private static final String CHANNEL_ID_KEY = "channelId";
  private static final String DESCRIPTION_KEY = "description";

  private String name;
  private String channelId;
  private String description;

  public ChannelRequest(String assistedName, String channelId, String description) {
    this.name = assistedName;
    this.channelId = channelId;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getChannelId() {
    return channelId;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString(NAME_KEY, name);
    bundle.putString(CHANNEL_ID_KEY, channelId);
    bundle.putString(DESCRIPTION_KEY, description);
    return bundle;
  }
}