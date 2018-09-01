package com.quartz.zielclient.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quartz.zielclient.exceptions.AuthorisationException;
import com.quartz.zielclient.models.User;

public final class UserController {
  private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

  private UserController() {
    // Intentionally empty
  }

  public static FirebaseUser retrieveFirebaseUser() throws AuthorisationException {
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    if (firebaseUser == null) {
      throw new AuthorisationException("Failed to find user.");
    }
    return firebaseUser;
  }

  public static void createUser(final FirebaseUser firebaseUser, String firstName, String lastName, User.Role role) {
    String userId = firebaseUser.getUid();
    String phoneNumber = firebaseUser.getPhoneNumber();
    User user = new User(firstName, lastName, phoneNumber, role);

    DatabaseReference ref = firebaseDatabase.getReference("users");
  }
}