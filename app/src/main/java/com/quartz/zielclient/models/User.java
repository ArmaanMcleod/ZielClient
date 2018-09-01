package com.quartz.zielclient.models;

/**
 * Model representing a user.
 */
public class User {
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private boolean isAssisted;

  public User(String firstName, String lastName, String phoneNumber, boolean isAssisted) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.isAssisted = isAssisted;
  }
}