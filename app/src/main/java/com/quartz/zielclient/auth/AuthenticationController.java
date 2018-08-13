package com.quartz.zielclient.auth;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

public final class AuthenticationController {

  private static final int TIMEOUT = 60;

  private final OnCompleteListener<AuthResult> signInCallback;
  private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private boolean verificationInProgress = false;
  private final OnVerificationStateChangedCallbacks mCallbacks = new OnVerificationStateChangedCallbacks() {

    @Override
    public void onVerificationCompleted(PhoneAuthCredential credential) {
      // This callback will be invoked in two situations:
      // 1 - Instant verification. In some cases the phone number can be instantly
      //     verified without needing to send or enter a verification code.
      // 2 - Auto-retrieval. On some devices Google Play services can automatically
      //     detect the incoming verification SMS and perform verification without
      //     user action.
      Log.d("", "onVerificationCompleted:" + credential);
      signInWithPhoneVerification(credential);
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
      // This callback is invoked in an invalid request for verification is made,
      // for instance if the the phone number format is not valid.
      Log.w("", "onVerificationFailed", e);

      if (e instanceof FirebaseAuthInvalidCredentialsException) {
        // Invalid request
        // ...
      } else if (e instanceof FirebaseTooManyRequestsException) {
      }

      // Show a message and update the UI
      // ...
    }

    @Override
    public void onCodeSent(String verificationId,
                           PhoneAuthProvider.ForceResendingToken token) {
      // The SMS verification code has been sent to the provided phone number, we
      // now need to ask the user to enter the code and then construct a credential
      // by combining the code with a verification ID.
      Log.d("", "onCodeSent:" + verificationId);

      // ...
    }
  };

  public AuthenticationController(OnCompleteListener<AuthResult> signInCallback) {
    this.signInCallback = signInCallback;
  }

  public void startAuthentication(final String phoneNumber, final Activity activity) {
    PhoneAuthProvider.getInstance().verifyPhoneNumber(
        phoneNumber,
        TIMEOUT,
        TimeUnit.SECONDS,
        activity,
        mCallbacks
    );
    verificationInProgress = true;
  }

  private void signInWithPhoneVerification(PhoneAuthCredential credential) {
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(signInCallback);
  }
}
