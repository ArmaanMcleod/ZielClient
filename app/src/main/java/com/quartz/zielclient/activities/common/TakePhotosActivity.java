package com.quartz.zielclient.activities.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.quartz.zielclient.R;

import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TakePhotosActivity extends Activity {
  private Button takePictureButton;
  private ImageView imageView;

  private Uri file;

  private static final int REQUEST_CODE = 99;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.take_photos);

    takePictureButton = findViewById(R.id.button_image);
    takePictureButton.setOnClickListener(this::takePicture);
    imageView = findViewById(R.id.imageview);

    if (ContextCompat.checkSelfPermission(this, CAMERA) != PERMISSION_GRANTED) {
      takePictureButton.setEnabled(false);
      ActivityCompat.requestPermissions(this, new String[] {CAMERA, WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED
          && grantResults[1] == PERMISSION_GRANTED) {
        takePictureButton.setEnabled(true);
      }
    }
  }

  public void takePicture(View view) {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    file = Uri.fromFile(getOutputMediaFile());
    intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

    startActivityForResult(intent, 100);
  }

  private static File getOutputMediaFile(){
    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES), "CameraDemo");

    if (!mediaStorageDir.exists()){
      if (!mediaStorageDir.mkdirs()){
        return null;
      }
    }

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    return new File(mediaStorageDir.getPath() + File.separator +
        "IMG_"+ timeStamp + ".jpg");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 100) {
      if (resultCode == RESULT_OK) {
        imageView.setImageURI(file);
      }
    }
  }
}
