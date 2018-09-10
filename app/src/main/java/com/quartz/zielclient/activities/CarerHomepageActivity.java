package com.quartz.zielclient.activities;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.icu.util.ChineseCalendar;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.adapters.ListAdapter;
import com.quartz.zielclient.models.ListItem;

import java.time.chrono.JapaneseEra;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarerHomepageActivity extends Activity implements ValueEventListener {

  private RecyclerView mRecyclerView;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private ArrayList<String> mItem;
  private List<ListItem> listItems;
  private DatabaseReference requestsReference;
  private String userID = "LglIRTsQqGUmpU16CuYJIxtS0S62";//getUserId
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_carer_homepage);

    // Getting requestsReference from FireBase
    requestsReference = FirebaseDatabase.getInstance().getReference("channelRequests/"+userID);
    requestsReference.addValueEventListener(this);

    // Initialising RecyclerView
    mRecyclerView = findViewById(R.id.my_recycler_view);

    // Each entry has fixed size.
    mRecyclerView.setHasFixedSize(true);

    // Use a linear layout manager
    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    listItems = new ArrayList<>();

    // Fake Data temporarily used
    for(int i=0; i<10; i++) {
      ListItem listItem = new ListItem(
          "Wei How",
          "needs to go to the hospital"
      );
      listItems.add(listItem);
    }

    mAdapter = new ListAdapter(listItems, this);
    mRecyclerView.setAdapter(mAdapter);

  }

  /**
   * Fetches the data as JSON files to
   * @param channelRequestsData
   */
  private void initData(Map<String, Object> channelRequestsData) {

    // Getting an ArrayList of all requests for this carer
    ArrayList<HashMap<String,String>> channelRequests = new ArrayList<>();
    channelRequestsData.forEach((channelRequestId,channelRequestValues) ->
        channelRequests.add((HashMap<String, String>) channelRequestValues));

  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    // Getting the channel data and calling the rendering method on it
    Map<String,Object> channelRequestsData = (Map<String, Object>) dataSnapshot;
    initData(channelRequestsData);
  }

  //TODO
  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {

  }
}
