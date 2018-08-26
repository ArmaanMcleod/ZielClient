package com.quartz.zielclient.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class YesNoDialog extends DialogFragment {
  public static final String ARG_TITLE = "YesNoDialog.Title";
  public static final String ARG_MESSAGE = "YesNoDialog.Message";

  public YesNoDialog() {

  }

  @Override
  public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
    Bundle args = getArguments();
    String title = args.getString(ARG_TITLE);
    String message = args.getString(ARG_MESSAGE);

    return new AlertDialog.Builder(getActivity())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.yes,
            (dialog, which) -> getParentFragment()
                .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null)
        )
        .setNegativeButton(android.R.string.no,
            (dialog, which) -> getParentFragment()
                .onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null)
        )
        .create();
  }

}
