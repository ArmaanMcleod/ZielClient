package com.quartz.zielclient.utilities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.quartz.zielclient.utilities.channel.Channel;
import com.quartz.zielclient.utilities.channel.ChannelListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChannelTest {
  @Mock
  ChannelListener channelListener;
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    channelListener = Mockito.mock(ChannelListener.class);
  }

  /**
   * Test that dataChange method is invoked on listener when channel updates
   */
  @Test
  public void testOnDataChange() {
    DataSnapshot testSnapShot = Mockito.mock(DataSnapshot.class);
    DatabaseReference databaseReference = Mockito.mock(DatabaseReference.class);
    Channel channel = new Channel(databaseReference,channelListener);
    channel.onDataChange(testSnapShot);
    Mockito.verify(channelListener).dataChanged();
  }
}
