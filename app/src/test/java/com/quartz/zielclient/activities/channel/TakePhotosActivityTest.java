package com.quartz.zielclient.activities.channel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 26)
public class TakePhotosActivityTest {
  TakePhotosActivity takePhotosActivity = Robolectric.buildActivity(TakePhotosActivity.class).create().get();

  // Check that its not null
  @Test
  public void testActivityNotNull() {
    assertNotNull(takePhotosActivity);
  }

  // Tests the dialogue which opens
  @Test
  public void testDialogueAppearsOnOpening() {
    assertNotNull(ShadowAlertDialog.getShownDialogs());
  }

  // Tests the back close button
  @Test
  public void testBackButtonClosesActivity() {

    ShadowActivity activityShadow = Shadows.shadowOf(takePhotosActivity);
    takePhotosActivity.onBackPressed();
    assertTrue(activityShadow.isFinishing());
  }


}