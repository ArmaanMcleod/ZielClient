package com.quartz.zielclient.activities.assisted;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.adapters.CarerSelectListAdapter;
import com.quartz.zielclient.models.CarerSelectionItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This activity allows the user to select another user to estabish a channel with
 *
 * @author Bilal
 */
public class AssistedSelectCarerActivity extends AppCompatActivity implements ValueEventListener {

  private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private RecyclerView mRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assisted_select_carer);

    Button addCarerActivity = findViewById(R.id.addCarerButton);
    addCarerActivity.setOnClickListener(
        v -> startActivity(new Intent(AssistedSelectCarerActivity.this, AddCarerActivity.class)));

    // Initialising RecyclerView
    mRecyclerView = findViewById(R.id.carer_list_recycler_view);

    // Each entry has fixed size.
    mRecyclerView.setHasFixedSize(true);

    // Use a linear layout manager
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    DatabaseReference requestsReference = FirebaseDatabase.getInstance()
        .getReference("relationships/" + firebaseAuth.getUid());
    requestsReference.addValueEventListener(this);
  }

  /**
   * Initialise the recycler view with some elements
   *
   * @param items
   */
  private void initData(Map<String, CarerSelectionItem> items) {
    List<CarerSelectionItem> list = items.entrySet()
        .stream()
        .peek(entry -> entry.getValue().setCarerId(entry.getKey()))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());

    // Using the Adapter to convert the data into the recycler view
    RecyclerView.Adapter mAdapter = new CarerSelectListAdapter(list, this);
    mRecyclerView.setAdapter(mAdapter);
  }

  /**
   * update the recycler view with user data
   *
   * @param dataSnapshot
   */
  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    GenericTypeIndicator<Map<String, CarerSelectionItem>> t =
        new GenericTypeIndicator<Map<String, CarerSelectionItem>>() {
        };
    Map<String, CarerSelectionItem> carerSelectionItems = dataSnapshot.getValue(t);
    if (carerSelectionItems != null) {
      initData(carerSelectionItems);
    }
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
  }
}