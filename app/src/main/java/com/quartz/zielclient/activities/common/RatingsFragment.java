package com.quartz.zielclient.activities.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.review.ReviewController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment that allows users to submit app reviews.
 * <p>
 * Activities that contain this fragment must implement the
 * {@link RatingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * @author alexvosnakis
 */
public class RatingsFragment extends Fragment implements View.OnClickListener, ValueEventListener {

  private static final String TAG = RatingsFragment.class.getSimpleName();

  private List<CheckBox> stars;
  private OnFragmentInteractionListener mListener;

  public RatingsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_ratings, container, false);
    Button submitReviewButton = view.findViewById(R.id.submitReview);
    submitReviewButton.setOnClickListener(this);

    // Add all the star checkboxes to the above list. Ordering is essential so the list is made
    // unmodifiable after creation
    List<CheckBox> tempStars = new ArrayList<>();
    tempStars.add(view.findViewById(R.id.starCheckBox));
    tempStars.add(view.findViewById(R.id.starCheckBox2));
    tempStars.add(view.findViewById(R.id.starCheckBox3));
    tempStars.add(view.findViewById(R.id.starCheckBox4));
    tempStars.add(view.findViewById(R.id.starCheckBox5));

    stars = Collections.unmodifiableList(tempStars);
    stars.forEach(star -> star.setOnClickListener(RatingsFragment.this));
    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  /**
   * Attempt to submit the user's review. Exits early if the view has presses 0 stars.
   */
  private void attemptSubmit() {
    long starsPressed = stars.stream()
        .filter(CompoundButton::isChecked)
        .count();

    // Can't submit with 0, have error
    if (starsPressed == 0) {
      Toast.makeText(getContext(), "A review must have at least 1 star.", Toast.LENGTH_LONG).show();
      return;
    }

    // v should never be null, as this is run after onCreateView
    View v = getView();
    if (v != null) {
      TextView reviewBox = v.findViewById(R.id.reviewBox);
      String reviewText = reviewBox.getText().toString();
      ReviewController.uploadReview((int) starsPressed, reviewText, this);
      onButtonPressed(Uri.EMPTY);
    }
  }

  /**
   * On click listener. The submit review button attempts to submit the review, otherwise the star
   * components
   *
   * @param v The view that was clicked.
   */
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.submitReview) {
      attemptSubmit();
      return;
    } else if (v.getId() == R.id.reviewBox) {
      return;
    }

    stars.forEach(star -> star.setChecked(false));
    for (CompoundButton starButton : stars) {
      starButton.setChecked(true);
      if (starButton.getId() == v.getId()) {
        break;
      }
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    Log.i(TAG, "Successfully submitted review.");
    Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_LONG).show();
    onButtonPressed(Uri.EMPTY);
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    Log.e(TAG, "Error uploading review", databaseError.toException());
    Toast.makeText(getContext(), "Error submitting review.", Toast.LENGTH_LONG).show();
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
