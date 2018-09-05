package com.quartz.zielclient.activities.assisted;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.user.UserController;
import com.quartz.zielclient.user.User;
import com.quartz.zielclient.user.UserFactory;

import java.util.Optional;

public class AssistedSelectCarer extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

  private TextView carerEntry;
  private User thisUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assisted_select_carer);

    carerEntry = findViewById(R.id.carerEntry);
    thisUser = UserFactory.getUser(getIntent().getBundleExtra("user"));
  }

  @Override
  public void onClick(View v) {
    if (v.getId() != R.id.confirmCarerButton) {
      return;
    }

    final String carerId = carerEntry.getText().toString();
    UserController.fetchUser(carerId, this);
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    if (!dataSnapshot.exists()) {
      carerEntry.setError("Invalid user");
      return;
    }

    Optional<User> maybeCarer = UserFactory.getUser(dataSnapshot);
    maybeCarer.ifPresent(carer -> {
      Intent intent = new Intent(AssistedSelectCarer.this, AssistedSession.class);
      intent.putExtra("assisted", thisUser.toBundle());
      intent.putExtra("carer", carer.toBundle());
      startActivity(intent);
      finish();
    });

    carerEntry.setError("Invalid user");
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    carerEntry.setError("Unknown error");
  }
}