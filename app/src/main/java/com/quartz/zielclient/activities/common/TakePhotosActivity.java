package com.quartz.zielclient.activities.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.quartz.zielclient.R;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

public class TakePhotosActivity extends AppCompatActivity {
  private CameraView cameraView;

  private boolean canTakePicture;

  private final String activity = this.getClass().getSimpleName();

  private final CameraKitEventListener cameraListener = new CameraKitEventListener() {
    @Override
    public void onEvent(CameraKitEvent cameraKitEvent) {
      String s = cameraKitEvent.getType();
      if (CameraKitEvent.TYPE_CAMERA_OPEN.equals(s)) {
        Log.d(activity, "Camera enabled");
        canTakePicture = true;

      } else if (CameraKitEvent.TYPE_CAMERA_CLOSE.equals(s)) {
        Log.d(activity, "Camera disabled");
        canTakePicture = false;
      }
    }

    @Override
    public void onError(CameraKitError cameraKitError) {
      Log.d(activity, "Error opening camera");
    }

    @Override
    public void onImage(CameraKitImage cameraKitImage) {
      Log.d(activity, "Taking picture");
      byte[] picture = cameraKitImage.getJpeg();
      Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
    }

    @Override
    public void onVideo(CameraKitVideo cameraKitVideo) {
      Log.d(activity, "Taking video");
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.take_photos);

    cameraView = findViewById(R.id.camera);
    cameraView.addCameraKitListener(cameraListener);

    cameraView.setFlash(CameraKit.Constants.FLASH_ON);
    cameraView.setFocus(CameraKit.Constants.FOCUS_TAP);
    cameraView.setMethod(CameraKit.Constants.METHOD_STILL);
    cameraView.setZoom(CameraKit.Constants.ZOOM_PINCH);
    cameraView.setPermissions(CameraKit.Constants.PERMISSIONS_STRICT);
    cameraView.setJpegQuality(100);

    Button takePhotoButton = findViewById(R.id.button);
    takePhotoButton.setOnClickListener(v -> {
      Log.d(activity, "Clicked button");

      if (canTakePicture) {
        Log.d(activity, "Captured image");
        cameraView.captureImage();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    cameraView.start();
  }

  @Override
  protected void onPause() {
    cameraView.stop();
    super.onPause();
  }
}
