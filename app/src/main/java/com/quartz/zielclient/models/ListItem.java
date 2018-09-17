package com.quartz.zielclient.models;

import android.os.Bundle;

/**
 * ListItem class for representing server data in any RecyclerView in the front-end.
 *
 * @author wei how ng
 */
public class ListItem implements Model {

  private String name;
  private String description;
  private String channelId;

  // Constructor
  public ListItem(String name, String description, String channelId) {
    this.name = name;
    this.description = description;
    this.channelId = channelId;
  }

  // Getters and Setters
  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getChannelId() {
    return channelId;
  }

  @Override
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString("name", name);
    bundle.putString("description", description);
    bundle.putString("channelId", channelId);
    return bundle;
  }
}
