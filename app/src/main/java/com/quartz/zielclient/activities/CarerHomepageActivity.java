package com.quartz.zielclient.activities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.adapters.ListAdapter;
import com.quartz.zielclient.models.ListItem;

import java.util.ArrayList;
import java.util.List;

public class CarerHomepageActivity extends Activity {

  private RecyclerView mRecyclerView;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private ArrayList<String> mItem;
  private List<ListItem> listItems;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_carer_homepage);

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
          "John" + (i+1),
          "needs to go to the hospital"
      );
      listItems.add(listItem);
    }

    mAdapter = new ListAdapter(listItems, this);
    mRecyclerView.setAdapter(mAdapter);

  }

  public void initData() {
    mItem = new ArrayList<String>();

  }

  // Getter for screen width.
  public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }
}
