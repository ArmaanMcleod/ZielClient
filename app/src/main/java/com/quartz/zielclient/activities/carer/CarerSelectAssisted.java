package com.quartz.zielclient.activities.carer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;

public class CarerSelectAssisted extends AppCompatActivity
        implements View.OnClickListener, ValueEventListener {

  private TextView channelIdEntry;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_carer_select_assisted);

    Button button = findViewById(R.id.confirmChannelId);
    button.setOnClickListener(this);

    channelIdEntry = findViewById(R.id.channelIdEntry);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() != R.id.confirmChannelId) {
      return;
    }

    String channelId = channelIdEntry.getText().toString();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("channels/" + channelId);

    ref.addListenerForSingleValueEvent(this);
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    if (!dataSnapshot.exists()) {
      channelIdEntry.setError("Invalid channel ID");
      return;
    }

    String channelId = channelIdEntry.getText().toString();
    Intent intent = new Intent(this, CarerChannel.class);
    intent.putExtra("channelId", channelId);
    startActivity(intent);
    finish();
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    // todo
  }
}
