package com.quartz.zielclient.activities.common;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.take_photos);

    cameraView = findViewById(R.id.camera);

    cameraView.setPermissions(CameraKit.Constants.PERMISSIONS_STRICT);

    cameraView.bindCameraKitListener(new CameraKitEventListener() {
      @Override
      public void onEvent(CameraKitEvent cameraKitEvent) {
        String s = cameraKitEvent.getType();
        if (CameraKitEvent.TYPE_CAMERA_OPEN.equals(s)) {
          canTakePicture = true;

        } else if (CameraKitEvent.TYPE_CAMERA_CLOSE.equals(s)) {
          canTakePicture = false;

        }
      }

      @Override
      public void onError(CameraKitError cameraKitError) {

      }

      @Override
      public void onImage(CameraKitImage cameraKitImage) {
        Bitmap result = cameraKitImage.getBitmap();
      }

      @Override
      public void onVideo(CameraKitVideo cameraKitVideo) {

      }
    });

    Button takePhotoButton = findViewById(R.id.button);
    takePhotoButton.setOnClickListener(v -> {
      if (canTakePicture) {
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
