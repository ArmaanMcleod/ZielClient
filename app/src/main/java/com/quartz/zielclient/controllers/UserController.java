package com.quartz.zielclient.controllers;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.exceptions.AuthorisationException;
import com.quartz.zielclient.models.User;

public final class UserController {

  /**
   * Path to the users in the database.
   */
  private static final String USER_DATABASE_PATH = "users";

  private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

  private UserController() {
    // Intentionally empty
  }

  /**
   * Attempts to return the currently logged in Firebase user.
   *
   * @return The Firebase user.
   * @throws AuthorisationException If the FirebaseUser could not be found.
   */
  public static FirebaseUser retrieveFirebaseUser() throws AuthorisationException {
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    if (firebaseUser == null) {
      throw new AuthorisationException("Failed to find user.");
    }
    return firebaseUser;
  }

  /**
   * Creates a user in the database. To keep it synced with the authentication database,
   *
   * @param firebaseUser A reference to the Firebase authenticated user.
   * @param firstName    The user's first name.
   * @param lastName     The user's last name.
   * @param isAssisted   Whether this user is an assisted.
   */
  public static User createUser(@NonNull FirebaseUser firebaseUser,
                                String firstName,
                                String lastName,
                                boolean isAssisted) {
    String userId = firebaseUser.getUid();
    String phoneNumber = firebaseUser.getPhoneNumber();
    User user = new User(firstName, lastName, phoneNumber, isAssisted);

    DatabaseReference ref = firebaseDatabase.getReference(USER_DATABASE_PATH);
    ref.child(userId).setValue(user);
    return user;
  }

  /**
   * Locates and performs an action with a user from the database.
   *
   * @param userId The UID of the user to look up.
   */
  public static void fetchUser(final String userId, ValueEventListener listener) {
    DatabaseReference ref = firebaseDatabase.getReference(userIdPath(userId));
    ref.addValueEventListener(listener);
  }

  private static String userIdPath(String userId) {
    return USER_DATABASE_PATH + "/" + userId;
  }
}