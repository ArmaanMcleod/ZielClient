package com.quartz.zielclient.channel;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * This Object abstracts away the communication with the database Channels
 * It requires a Chanlelistener to be able to pass updates to the user.
 *
 * @author Bilal Shehata
 */
public class ChannelData implements ValueEventListener {
  // contains current channel values in database
  private Map<String, Object> channelValues;
  // reference to the database
  private DatabaseReference channelReference;
  // the object that wants to listen to this channel
  private ChannelListener channelListener;


  /**
   * @param channelReference - location in database where channel exists
   * @param channelListener  - the object that wants to listen to the channel
   */
  public ChannelData(DatabaseReference channelReference, ChannelListener channelListener) {
    this.channelReference = channelReference;
    this.channelListener = channelListener;
    channelReference.addValueEventListener(this);
  }

  public String getAssisted() {
    return channelValues.get("assisted").toString();
  }

  public void setAssisted(String assisted) {
    channelReference.child("assisted").setValue(assisted);
  }

  public Boolean getAssistedStatus() {
    return channelValues.get("assistedStatus").equals(true);
  }

  public void setAssistedStatus(Boolean assistedStatus) {
    channelReference.child("assistedStatus").setValue(assistedStatus);
  }

  public String getCarer() {
    return channelValues.get("carer").toString();
  }

  public void setCarer(String carer) {
    this.channelReference.child(carer).setValue(carer);
  }

  public Boolean getCarerStatus() {
    return channelValues.get("carerStatus").equals(true);
  }

  public void setCarerStatus(Boolean carerStatus) {
    channelReference.child("carerStatus").setValue(carerStatus);
  }

  public Boolean getPing() {
    return channelValues.get("Ping").equals(true);
  }

  public void setPing(Boolean ping) {
    channelReference.child("Ping").setValue(ping);
  }

  public DatabaseReference getChannelReference() {
    return channelReference;
  }

  public void setChannelReference(DatabaseReference channelReference) {
    this.channelReference = channelReference;
  }

  public ChannelListener getChannelListener() {
    return channelListener;
  }

  public void setChannelListener(ChannelListener channelListener) {
    this.channelListener = channelListener;
  }

  /**
   * recieve update from database and update the listener that some data has changed
   *
   * @param dataSnapshot
   */
  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
    channelValues = dataSnapshot.getValue(t);
    channelListener.dataChanged();
  }

  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {
    //todo
  }
}
