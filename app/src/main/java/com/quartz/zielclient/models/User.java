package com.quartz.zielclient.models;

import java.util.Map;

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

  public User(Map<String, Object> rawUserData) {
    this.firstName = (String) rawUserData.get("firstName");
    this.lastName = (String) rawUserData.get("lastName");
    this.phoneNumber = (String) rawUserData.get("phoneNumber");
    this.isAssisted = (Boolean) rawUserData.get("isAssisted");
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public boolean isAssisted() {
    return isAssisted;
  }

  @Override
  public String toString() {
    return "User{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", isAssisted=" + isAssisted +
        '}';
  }
}