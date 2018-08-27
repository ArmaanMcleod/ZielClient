package com.quartz.zielclient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class YesNoDialog extends DialogFragment {

  public static final String ARG_TITLE = "YesNoDialog.Title";
  public static final String ARG_MESSAGE = "YesNoDialog.Message";
  public static final String ARG_NO_CALLBACK = "YesNoDialog.NoCallback";
  public static final String ARG_YES_CALLBACK = "YesNoDialog.YesCallback";

  public YesNoDialog() {
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
    Bundle args = getArguments();
    String title = args.getString(ARG_TITLE);
    String message = args.getString(ARG_MESSAGE);

    return new AlertDialog.Builder(getActivity())
        .setTitle(title)
        .setMessage(message)
        .create();
  }
}