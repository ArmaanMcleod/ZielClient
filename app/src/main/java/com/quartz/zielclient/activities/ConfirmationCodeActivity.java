package com.quartz.zielclient.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.quartz.zielclient.R;
import com.quartz.zielclient.controllers.AuthorisationController;

import static com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import static com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;


public class ConfirmationCodeActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {

  private static final String TAG = "ConfirmationCodeActivity";

  private AuthorisationController authController;

  private String phoneNumber;
  private String mVerificationId;
  private ForceResendingToken mResendingToken;

  private TextView verificationField;

  private final OnVerificationStateChangedCallbacks callbacks = new OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(PhoneAuthCredential credential) {
      Log.d(TAG, "onVerificationCompleted:" + credential);
      authController.signInWithPhoneAuthCredential(credential, ConfirmationCodeActivity.this);
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
      Toast feedback = Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
      Log.d("code exception", "an error occured:", e);
      feedback.show();
    }

    @Override
    public void onCodeSent(String verificationId, ForceResendingToken token) {
      // The SMS verification code has been sent to the provided phone number, we
      // now need to ask the user to enter the code and then construct a credential
      // by combining the code with a verification ID.
      Log.d(TAG, "onCodeSent:" + verificationId);

      // Save verification ID and resending token so we can use them later
      mVerificationId = verificationId;
      mResendingToken = token;
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_confirmation_code);
    verificationField = findViewById(R.id.confirmationCodeEntry);

    phoneNumber = getIntent().getStringExtra("phoneNumber");
    Log.d("Phone number", phoneNumber);

    authController = new AuthorisationController(phoneNumber, this);
    authController.sendConfirmationCode(callbacks);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.confirmCodeButton:
        verifyPhoneNumberWithCode();
        break;
      case R.id.resend_button:
        authController.resendConfirmationCode(callbacks, mResendingToken);
        break;
      default:
        break;
    }
  }

  private void verifyPhoneNumberWithCode() {
    String code = verificationField.getText().toString();
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
    authController.signInWithPhoneAuthCredential(credential, this);
  }

  @Override
  public void onComplete(@NonNull Task<AuthResult> task) {
    if (task.isSuccessful()) {
      // Sign in success, update UI with the signed-in user's information
      Log.d(TAG, "signInWithCredential:success");
      FirebaseUser user = task.getResult().getUser();
      Toast toast = Toast.makeText(this, "user signed in:" + user.getPhoneNumber(), Toast.LENGTH_SHORT);
      toast.show();
    } else {
      // Sign in failed, display a message and update the UI
      Log.w(TAG, "signInWithCredential:failure", task.getException());
      if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
        verificationField.setError("Invalid code.");
      }
    }
  }
}