package com.quartz.zielclient.controllers;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.exceptions.AuthorisationException;
import com.quartz.zielclient.exceptions.UserNotFoundException;
import com.quartz.zielclient.models.User;

import java.util.Map;
import java.util.Optional;

public final class UserController {
  private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

  private UserController() {
    // Intentionally empty
  }

  /**
   * Attempts to return the currently logged in Firebase user.
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
  public static void createUser(@NonNull FirebaseUser firebaseUser,
                                String firstName,
                                String lastName,
                                boolean isAssisted) {
    String userId = firebaseUser.getUid();
    String phoneNumber = firebaseUser.getPhoneNumber();
    User user = new User(firstName, lastName, phoneNumber, isAssisted);

    DatabaseReference ref = firebaseDatabase.getReference("users");
    ref.child(userId).setValue(user);
  }

  /**
   * Locates and returns a user from the database.
   *
   * @param userId The UID of the user to look up.
   * @return A User model.
   * @throws UserNotFoundException If there was an error when finding the user, or if the user could
   *                               not be found.
   */
  public static User fetchUser(final String userId) throws UserNotFoundException {
    DatabaseReference ref = firebaseDatabase.getReference("users/" + userId);
    UserRetriever retriever = new UserRetriever();
    ref.addValueEventListener(retriever);
    Optional<User> user = retriever.user;

    if (user.isPresent()) {
      return user.get();
    } else {
      throw new UserNotFoundException("User " + userId + " could not be found.");
    }
  }

  /**
   * Because the Firebase API fucking sucks we can't just return data, we have to do this just to
   * read the data somewhere else. Fuck my life
   *
   * Actually we can't because it's asynchronous. Why would I ever want synchronous code I guess.
   */
  private static final class UserRetriever implements ValueEventListener {
    private Optional<User> user;

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
      GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
      };
      Map<String, Object> rawUserData = dataSnapshot.getValue(t);
      if (rawUserData == null) {
        user = Optional.empty();
      } else {
        user = Optional.of(new User(rawUserData));
      }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
      user = Optional.empty();
    }
  }
}