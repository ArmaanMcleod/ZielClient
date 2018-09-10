package com.quartz.zielclient.user;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.quartz.zielclient.models.Model;

import java.util.Map;

/**
 * Model representing a user.
 */
public class User implements Model {

  static final String FIRST_NAME_KEY = "firstName";
  static final String LAST_NAME_KEY = "lastName";
  static final String PHONE_NUMBER_KEY = "phoneNumber";
  static final String IS_ASSISTED_KEY = "assisted";

  private String firstName;
  private String lastName;
  private String phoneNumber;
  private boolean isAssisted;

  User(String firstName, String lastName, String phoneNumber, boolean isAssisted) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.isAssisted = isAssisted;
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