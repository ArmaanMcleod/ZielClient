package com.quartz.zielclient.models;

import android.os.Bundle;

import java.util.Map;

/**
 * Model representing a user.
 */
public class User implements Model {

  private static final String FIRST_NAME_KEY = "firstName";
  private static final String LAST_NAME_KEY = "lastName";
  private static final String PHONE_NUMBER_KEY = "phoneNumber";
  private static final String IS_ASSISTED_KEY = "isAssisted";

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
    this.firstName = (String) rawUserData.get(FIRST_NAME_KEY);
    this.lastName = (String) rawUserData.get(LAST_NAME_KEY);
    this.phoneNumber = (String) rawUserData.get(PHONE_NUMBER_KEY);
    this.isAssisted = (Boolean) rawUserData.get(IS_ASSISTED_KEY);
  }

  public User(Bundle bundle) {
    this.firstName = bundle.getString(FIRST_NAME_KEY);
    this.lastName = bundle.getString(LAST_NAME_KEY);
    this.phoneNumber = bundle.getString(PHONE_NUMBER_KEY);
    this.isAssisted = bundle.getBoolean(IS_ASSISTED_KEY);
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
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString(FIRST_NAME_KEY, firstName);
    bundle.putString(LAST_NAME_KEY, lastName);
    bundle.putString(PHONE_NUMBER_KEY, phoneNumber);
    bundle.putBoolean(IS_ASSISTED_KEY, isAssisted);
    return bundle;
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