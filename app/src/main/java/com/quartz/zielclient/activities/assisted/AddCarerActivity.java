package com.quartz.zielclient.activities.assisted;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quartz.zielclient.R;
import com.quartz.zielclient.request.AddCarerRequestHandler;

/**
 * Activity allows user to add a permanent carer
 *
 * @author Bilal Shehata
 */
public class AddCarerActivity extends AppCompatActivity
    implements View.OnClickListener, CarerRequestListener {

  private EditText inputNumber;
  private AddCarerRequestHandler addCarerRequestHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addCarerRequestHandler = new AddCarerRequestHandler();

    setContentView(R.layout.activity_add_carer);
    Button sendRequest = findViewById(R.id.sendRequestButton);
    sendRequest.setOnClickListener(this);
    inputNumber = findViewById(R.id.carerNumberInput);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case (R.id.sendRequestButton):
        addCarerRequestHandler.addCarer(inputNumber.getText().toString(), this);
        break;
      default:
        break;
    }
  }

  /** display error if user is not found in DB */
  @Override
  public void userNotFound() {
    Toast.makeText(getApplicationContext(), R.string.userNotFound, Toast.LENGTH_LONG).show();
  }

  /** Return to home if user is found and added */
  @Override
  public void userFound() {
    Toast.makeText(getApplicationContext(), R.string.userAdded, Toast.LENGTH_LONG).show();
    onBackPressed();
  }
}
