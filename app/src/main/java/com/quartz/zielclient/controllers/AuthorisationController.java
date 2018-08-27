package com.quartz.zielclient.controllers;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

public final class AuthorisationController {

  public static void verifyPhoneNumber(
      String phoneNumber,
      Activity activity,
      OnVerificationStateChangedCallbacks callbacks) {
    Log.d("AuthorisationController ", "sending phone number:" + phoneNumber);

    PhoneAuthProvider.getInstance().verifyPhoneNumber(
        phoneNumber,
        60L,
        TimeUnit.SECONDS,
        activity,
        callbacks
    );
  }
}
