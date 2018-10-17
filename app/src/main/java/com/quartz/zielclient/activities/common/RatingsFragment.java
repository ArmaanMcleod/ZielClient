package com.quartz.zielclient.activities.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.review.ReviewController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RatingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RatingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingsFragment extends Fragment implements View.OnClickListener, ValueEventListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private List<CheckBox> stars = new ArrayList<>();

  private OnFragmentInteractionListener mListener;

  public RatingsFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment RatingsFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static RatingsFragment newInstance(String param1, String param2) {
    RatingsFragment fragment = new RatingsFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_ratings, container, false);

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

  private void attemptSubmit() {
    long starsPressed = stars.stream()
        .filter(CompoundButton::isChecked)
        .count();
    if (starsPressed == 0) {
      // Can't submit with 0, have error
      return;
    }

    // v should never be null, as this is run after onCreateView
    View v = getView();
    if (v != null) {
      TextView reviewBox = v.findViewById(R.id.reviewBox);
      String reviewText = reviewBox.getText().toString();
      ReviewController.uploadReview((int) starsPressed, reviewText, this);
    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.submitReview) {
      attemptSubmit();
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

  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {

  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
