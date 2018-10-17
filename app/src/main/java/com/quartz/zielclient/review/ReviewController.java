package com.quartz.zielclient.review;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.user.UserController;

import java.util.Optional;

/**
 * Controller for reviews.
 */
public class ReviewController {

  private static final String REVIEWS_PATH = "reviews";
  private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

  private ReviewController() {
    // Intentionally empty
  }

  /**
   * Uploads a user's review.
   *
   * @param stars      The number of stars given to the review.
   * @param reviewText The user's review comment.
   * @param listener   The listener waiting for an update.
   */
  public static void uploadReview(int stars, String reviewText, ValueEventListener listener) {
    Review review = new Review(stars, reviewText);
    Optional<String> maybeId = UserController.retrieveUid();
    maybeId.ifPresent(userId -> {
      DatabaseReference ref = database.getReference(REVIEWS_PATH);
      ref.addListenerForSingleValueEvent(listener);
      ref.child(userId).push().setValue(review);
    });
  }

  /**
   * Fetches a user's reviews and passes it to a listener.
   *
   * @param listener The listener to receive the review.
   */
  public static void fetchReviews(ValueEventListener listener) {
    Optional<String> maybeId = UserController.retrieveUid();
    maybeId.ifPresent(userId -> {
      DatabaseReference ref = database.getReference(REVIEWS_PATH);
      ref.addListenerForSingleValueEvent(listener);
    });
  }
}