package com.quartz.zielclient.activities.assisted;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.quartz.zielclient.R;
import com.quartz.zielclient.request.AddCarerRequestHandler;

public class AddCarerActivity extends AppCompatActivity implements View.OnClickListener {

  EditText inputNumber;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_carer);
    Button sendRequest = findViewById(R.id.sendRequestButton);
    sendRequest.setOnClickListener(this);
    inputNumber = findViewById(R.id.carerNumberInput);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case (R.id.sendRequestButton):
        AddCarerRequestHandler.addCarer(inputNumber.getText().toString());
        break;
      default:
        break;
    }
  }
}
