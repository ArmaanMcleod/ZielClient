package com.quartz.zielclient.models;

/**
 * ListItem class for representing server data in any RecyclerView in the front-end.
 *
 * @author wei how ng
 */
public class ListItem {

  private String name;
  private String description;
  private String channelID;

  // Constructor
  public ListItem(String name, String description) {
    this.name = name;
    this.description = description;
  }

  // Getters and Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getChannelID() {
    return channelID;
  }

  public void setChannelID(String channelID) {
    this.channelID = channelID;
  }
}
