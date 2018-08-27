package com.quartz.zielclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.quartz.zielclient.R;
import com.quartz.zielclient.controllers.AuthorisationController;

import static com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;


public class ConfirmationCodeActivity extends AppCompatActivity {

  private String phoneNumber;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_confirmation_code);

    phoneNumber = getIntent().getStringExtra("phoneNumber");
    Log.d("Phone number", phoneNumber);
    AuthorisationController.verifyPhoneNumber(phoneNumber, this, callbacks);

  }

  private final OnVerificationStateChangedCallbacks callbacks = new OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
      Toast feedback = Toast.makeText(getApplicationContext(), "Verification completed.", Toast.LENGTH_SHORT);
      feedback.show();
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
      Toast feedback = Toast.makeText(getApplicationContext(), "An error occured.", Toast.LENGTH_SHORT);
      Log.d("code exception", "an error occured:", e);
      feedback.show();
    }

    @Override
    public void onCodeSent(String verificationId,
                           PhoneAuthProvider.ForceResendingToken token) {
      Toast feedback = Toast.makeText(getApplicationContext(), "code sent.", Toast.LENGTH_SHORT);
      feedback.show();
    }
  };


}
