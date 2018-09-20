package com.quartz.zielclient.request;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCarerRequestHandler {
  private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
  private static DatabaseReference relationshipReference =
          firebaseDatabase.getReference("relationships/");
  private static DatabaseReference userReference = firebaseDatabase.getReference("users/");

  public static Boolean addCarer(String phoneNumber) {
    userReference
            .orderByChild("phoneNumber")
            .equalTo(phoneNumber)
            .addListenerForSingleValueEvent(
                    new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // since mobile numbers are unique this should only return a single user
                        dataSnapshot
                                .getChildren()
                                .forEach(
                                        (user) -> {
                                          String carerId = user.getKey();
                                          relationshipReference
                                                  .child(firebaseAuth.getUid())
                                                  .child(carerId)
                                                  .setValue(user.getValue());
                                        });
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {
                      }
                    });
    return false;
  }
}
