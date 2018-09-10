package com.quartz.zielclient.activities.carer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.user.UserController;

import java.util.Optional;

public class CarerSelectAssisted extends AppCompatActivity implements ValueEventListener {

  private String uid;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_carer_select_assisted);

    Optional<String> userId = UserController.retrieveUid();
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {

  }
}
