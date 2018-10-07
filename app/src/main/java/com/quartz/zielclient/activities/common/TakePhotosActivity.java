package com.quartz.zielclient.activities.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.quartz.zielclient.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakePhotosActivity extends AppCompatActivity {

  ImageView imageView;
  Button button;
  File photoFile = null;
  static final int CAPTURE_IMAGE_REQUEST = 1;


  String mCurrentPhotoPath;
  private static final String IMAGE_DIRECTORY_NAME = "VLEMONN";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.take_photos);

    imageView =  findViewById(R.id.imageView);
    button = findViewById(R.id.button_image);

    button.setOnClickListener(view -> {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        captureImage();
      }
      else
      {
        captureImage2();
      }
    });
  }

  /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
  private void captureImage2() {

    try {
      Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
      photoFile = createImageFile4();
      if(photoFile!=null)
      {
        displayMessage(getBaseContext(),photoFile.getAbsolutePath());
        Log.i("Mayank",photoFile.getAbsolutePath());
        Uri photoURI  = Uri.fromFile(photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
      }
    }
    catch (Exception e)
    {
      displayMessage(getBaseContext(),"Camera is not available."+e.toString());
    }
  }

  private void captureImage()
  {

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
    }
    else
    {
      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        // Create the File where the photo should go
        try {

          photoFile = createImageFile();
          displayMessage(getBaseContext(),photoFile.getAbsolutePath());
          Log.i("Mayank",photoFile.getAbsolutePath());

          // Continue only if the File was successfully created
          if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                "com.vlemonn.blog.captureimage.fileprovider",
                photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
          }
        } catch (Exception ex) {
          // Error occurred while creating the File
          displayMessage(getBaseContext(),ex.getMessage().toString());
        }


      }else
      {
        displayMessage(getBaseContext(),"Nullll");
      }
    }



  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
      Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
      imageView.setImageBitmap(myBitmap);
    }
    else
    {
      displayMessage(getBaseContext(),"Request cancelled or something went wrong.");
    }
  }

  private File createImageFile4()
  {
    // External sdcard location
    File mediaStorageDir = new File(
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        IMAGE_DIRECTORY_NAME);
    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        displayMessage(getBaseContext(),"Unable to create directory.");
        return null;
      }
    }

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
        Locale.getDefault()).format(new Date());
    File mediaFile = new File(mediaStorageDir.getPath() + File.separator
        + "IMG_" + timeStamp + ".jpg");

    return mediaFile;

  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private void displayMessage(Context context, String message)
  {
    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == 0) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        captureImage();
      }
    }

  }
}
