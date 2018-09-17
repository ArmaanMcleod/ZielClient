package com.quartz.zielclient.channel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.models.ChannelRequest;
import com.quartz.zielclient.user.User;

public class RequestController {

  private static final String TAG = RequestController.class.getSimpleName();
  private static final String REQUESTS_PATH = "channelRequests";
  private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

  private RequestController() {
    // Intentionally empty
  }

  /**
   * Creates an instance of a channel request in the database.
   * The new request has an identifier equal the current number of requests a user has.
   *
   * @param assisted The assisted user creating the request.
   * @param carerId The ID of the carer the request is intended for.
   * @param channelId The ID of the channel created.
   * @param desc The description of the request.
   */
  public static void createRequest(User assisted, String carerId, String channelId, String desc) {
    Log.i(TAG, String.format("Creating channel request from %1s to %2s", assisted.fullName(), carerId));

    DatabaseReference reference = database.getReference(REQUESTS_PATH).child(carerId);
    ChannelRequest request = new ChannelRequest(assisted.fullName(), channelId, desc);

    reference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Long count = dataSnapshot.getChildrenCount();
        reference.child(count.toString()).setValue(request);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e(TAG, "Error reading from database", databaseError.toException());
      }
    });
  }
}
