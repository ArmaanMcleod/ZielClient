package com.quartz.zielclient;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;

public class CarerHomepageActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // Initialising Bitmap Tiles
    int width = getScreenWidth();
    Bitmap mapBitmap = Bitmap.createBitmap(width, 100, Bitmap.Config.ALPHA_8);
    Canvas c = new Canvas(mapBitmap);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_carer_homepage);
  }

  // Getter for screen width.
  public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

}
