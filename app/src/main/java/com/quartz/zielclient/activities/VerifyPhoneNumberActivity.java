package com.quartz.zielclient.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.fragments.YesNoDialog;
import com.quartz.zielclient.services.SystemService;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.view.View.OnClickListener;

public class VerifyPhoneNumberActivity extends AppCompatActivity implements OnClickListener  {

  private TextView phoneNumberEntry;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verify_phone_number);

    requestPermissions(new String[]{READ_PHONE_STATE}, 1);
    populatePhoneNumber();

    Button confirmButton = findViewById(R.id.confirmNumber);
    confirmButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    final int clickedId = view.getId();
    if (clickedId != R.id.confirmNumber) {
      // We only want to handle this for confirmations.
      return;
    }

    displayConfirmationPrompt();
  }

  private void populatePhoneNumber() {
    phoneNumberEntry = findViewById(R.id.phoneEntry);
    phoneNumberEntry.setText(SystemService
        .retrieveSystemPhoneNumber(this)
        .orElse("")
    );
  }

  private void displayConfirmationPrompt() {
    DialogFragment dialog = new YesNoDialog();
    Bundle args = new Bundle();
    args.putString(YesNoDialog.ARG_TITLE, "Confirm your number");
    args.putString(YesNoDialog.ARG_MESSAGE, String.format("Is %s your phone number?", phoneNumberEntry.getText()));
    dialog.setArguments(args);
    dialog.show(getSupportFragmentManager(), "tag");
  }
}
