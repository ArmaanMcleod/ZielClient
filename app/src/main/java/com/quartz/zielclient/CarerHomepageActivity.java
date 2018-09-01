package com.quartz.zielclient;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;

public class CarerHomepageActivity extends Activity {

  private int height;
  private int width;


  private Bitmap mapBitmap = Bitmap.createBitmap(width, 100, Bitmap.Config.ALPHA_8);
  private Canvas c = new Canvas(mapBitmap);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    this.height = displayMetrics.heightPixels;
    this.width = displayMetrics.widthPixels;

    setContentView(R.layout.activity_carer_homepage);
  }
}
