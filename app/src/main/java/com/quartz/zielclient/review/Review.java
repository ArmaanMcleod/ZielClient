package com.quartz.zielclient.review;

import android.os.Bundle;

import com.quartz.zielclient.models.Model;

/**
 * Model representation of a user review.
 *
 * @author alexvosnakis
 */
public class Review implements Model {
  private int stars;
  private String reviewText;

  public Review() {
    // Intentionally empty
  }

  public Review(int stars, String reviewText) {
    this.stars = stars;
    this.reviewText = reviewText;
  }

  public int getStars() {
    return stars;
  }

  public void setStars(int stars) {
    this.stars = stars;
  }

  public String getReviewText() {
    return reviewText;
  }

  public void setReviewText(String reviewText) {
    this.reviewText = reviewText;
  }

  @Override
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putInt("stars", stars);
    bundle.putString("reviewText", reviewText);
    return bundle;
  }
}
