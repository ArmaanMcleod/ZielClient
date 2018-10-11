package com.quartz.zielclient.activities.carer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.common.StreetViewActivity;
import com.quartz.zielclient.activities.common.TextChatActivity;
import com.quartz.zielclient.activities.common.VideoActivity;
import com.quartz.zielclient.activities.common.VoiceActivity;
import com.quartz.zielclient.channel.ChannelController;
import com.quartz.zielclient.channel.ChannelData;
import com.quartz.zielclient.channel.ChannelListener;
import com.quartz.zielclient.map.FetchUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity Reads coordinate and route information from a channel and displays a Map accordigly
 * THis allows a carer to have upto date locative information from the assisted
 *
 * @author Bilal Shehata
 */
public class CarerMapsActivity extends AppCompatActivity
    implements OnMapReadyCallback,
        ChannelListener,
        View.OnClickListener,
        GoogleMap.OnMapClickListener {

  // These constants are displayed until map syncronizes (only momentarily)
  // This prevents the default usage of  0,0
  private final double MELBOURNEUNILAT = -37.7964;
  private final double MELBOURNEUNILONG = 144.9612;
  // initialize assisted location marker
  private final MarkerOptions assistedMarkerOptions = new MarkerOptions();
  AlertDialog alertDialog;
  private String channelId;
  private GoogleMap mGoogleMap;
  private Button dropMarkers;
  private Button clearMarkers;
  private String currentDestinationURL = "none";
  // default to melbourne uni
  // list of Assisted movements
  private Double[] latitude = {MELBOURNEUNILAT};
  private Double[] longitude = {MELBOURNEUNILONG};
  private Button toTextChat;
  private Button toVoiceChat;
  private String key;
  private List<Marker> markers;
  private Marker assistedMarker;

  // debug channel to be replaced with the current channel that was handled by a previous activity.
  private ChannelData channel;
  /**
   * Initizialise the activity
   *
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set xml view file
    setContentView(R.layout.activity_carer_maps);
    key = "&key=" + getApplicationContext().getString(R.string.google_api_key);
    markers = new ArrayList<>();
    toTextChat = findViewById(R.id.toTextChat);
    toVoiceChat = findViewById(R.id.toVoiceChat);
    dropMarkers = findViewById(R.id.dropMarker);
    clearMarkers = findViewById(R.id.clearMarker);
    Button toVideoChat = findViewById(R.id.toVideoActivity);
    dropMarkers.setOnClickListener(this);
    clearMarkers.setOnClickListener(this);
    toVideoChat.setOnClickListener(this);
    toVoiceChat.setOnClickListener(this);
    alertDialog = makeVideoAlert();
    toTextChat.setOnClickListener(this);
    channelId = getIntent().getStringExtra(getApplicationContext().getString(R.string.channel_key));
    if (channelId != null) {
      channel = ChannelController.retrieveChannel(channelId, this);
    }
    Intent intentVoice = new Intent(CarerMapsActivity.this, VoiceActivity.class);
    intentVoice.putExtra("initiate", 1);
    startActivity(intentVoice);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    if (mapFragment != null) {
      mapFragment.getMapAsync(this);
    }
  }

  /**
   * Manipulates the map once available. Once map is ready add a temporary marker (once again The
   * University of Melbourne is used temporarily rather than the default NUll-Island)
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    // place a marker on melbourne uni whilst synchronization occurs
    assistedMarkerOptions.position(new LatLng(MELBOURNEUNILAT, MELBOURNEUNILONG));
    assistedMarkerOptions.title("Assisted Location");
    assistedMarker = mGoogleMap.addMarker(assistedMarkerOptions);
    updateMapCoords();
    mGoogleMap.setOnMarkerClickListener(
        marker -> {
          marker.showInfoWindow();

          // Prompt Street view
          new AlertDialog.Builder(this)
              .setIcon(R.drawable.street_view_logo)
              .setTitle("Google Maps Street View")
              .setMessage("Show street view?")

              // Start Street view activity when pressed
              .setPositiveButton(
                  "Yes",
                  (dialog, which) -> {
                    Intent intent = new Intent(CarerMapsActivity.this, StreetViewActivity.class);
                    intent.putExtra("destination", marker.getPosition());
                    startActivity(intent);
                  })
              .setNegativeButton("No", (dialog, which) -> {})
              .show();
          return true;
        });
  }

  /** Update the Coordinates based on the latest Assisted's location */
  public void updateMapCoords() {
    LatLng assistedLocation = new LatLng(latitude[0], longitude[0]);
    // Safety check
    // Asynchronous map may lead to error this ensures that the database call
    // does update if map is not ready
    if (assistedMarker != null) {
      assistedMarker.setPosition(assistedLocation);
      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(assistedLocation));
      mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(assistedLocation, 15));
    }
  }

  /** THis listens to changes in the channel Once a change occurs update the long/lat values */
  @Override
  public void dataChanged() {
    // Update location
    latitude[0] = channel.getAssistedLocation().latitude;
    longitude[0] = channel.getAssistedLocation().longitude;
    updateMapCoords();

    if (channel != null) {
      channel.setCarerStatus(true);
      // if the assisted has entered a route then generate that same route
      if ((channel.getDirectionsURL() != null)
          && !channel.getDirectionsURL().equals("none")
          && !channel.isChannelEnded()) {
        // if the route is already the current route then don't update
        if (!channel.getDirectionsURL().equals(currentDestinationURL)) {
          // update the route
          if (mGoogleMap != null) {
            mGoogleMap.clear();
            mGoogleMap.addMarker(new MarkerOptions().position(channel.getAssistedLocation()));

            Log.d("DIRECTIONS", channel.getDirectionsURL());
            FetchUrl fetchUrl = new FetchUrl(mGoogleMap);
            fetchUrl.execute(channel.getDirectionsURL() + key);
            currentDestinationURL = channel.getDirectionsURL();
          }
        }
      }
      if (channel.isChannelEnded()) {
        if(!this.isFinishing())
        {
          makeChannelEndedAlert();
        }
      }
      if (channel.getVideoCallStatus()) {
        alertDialog.show();
      } else {
        alertDialog.cancel();
      }
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.toTextChat:
        Intent intentToTextChat = new Intent(CarerMapsActivity.this, TextChatActivity.class);
        intentToTextChat.putExtra(
            getApplicationContext().getString(R.string.channel_key), channelId);
        startActivity(intentToTextChat);
        break;
      case R.id.toVoiceChat:
        Intent intentVoice = new Intent(CarerMapsActivity.this, VoiceActivity.class);
        if (channel != null) {
          intentVoice.putExtra("initiate", 0);
          intentVoice.putExtra("CallId", channel.getAssisted());
        }
        startActivity(intentVoice);
        break;
      case R.id.toVideoActivity:
        Intent intentToVideo = new Intent(CarerMapsActivity.this, VideoActivity.class);
        intentToVideo.putExtra(getResources().getString(R.string.channel_key), channelId);
        startActivity(intentToVideo);
        break;
      case R.id.dropMarker:
        mGoogleMap.setOnMapClickListener(this);
        Toast.makeText(this, "Press on map now", Toast.LENGTH_LONG).show();
        break;
      case R.id.clearMarker:
        deleteMarkers();
        break;
      default:
        break;
    }
  }

  @Override
  public void onBackPressed() {
    VoiceActivity.endCall();

    super.finish();
  }

  public AlertDialog makeVideoAlert() {
    alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle("Video Share?");
    alertDialog.setMessage("Carer wants to share video with you  please also join the channel");
    alertDialog.setButton(
        AlertDialog.BUTTON_NEUTRAL,
        "OK",
        (dialog, which) -> {
          Intent intentToVideo = new Intent(getApplicationContext(), VideoActivity.class);
          intentToVideo.putExtra(
              getApplicationContext().getResources().getString(R.string.channel_key), channelId);
          getApplicationContext().startActivity(intentToVideo);
        });
    return alertDialog;
  }

  private void deleteMarkers() {
    markers.forEach(Marker::remove);
    markers.clear();
    channel.clearMarkers();
  }

  /**
   * Creates a marker and shows it on the Google map.
   *
   * @param location The location of marker.
   * @param colour The colour of marker.
   * @return Marker The marker object.
   */
  private void placeMarker(LatLng location, float colour) {
    MarkerOptions markerOptions =
        new MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(colour));
    Marker newMarker = mGoogleMap.addMarker(markerOptions);
    markers.add(newMarker);
  }

  @Override
  public void onMapClick(LatLng latLng) {
    channel.addMarker(latLng);
    placeMarker(latLng, 255);
    mGoogleMap.setOnMapClickListener(null);
  }

  public void makeChannelEndedAlert() {
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle("Channel has finished");
    alertDialog.setMessage("This channel has been ended. Will now return to home page");
    alertDialog.setButton(
        AlertDialog.BUTTON_NEUTRAL,
        "OK",
        (dialog, which) -> {
          channel.endChannel();
          alertDialog.dismiss();
          Intent intent = new Intent(getApplicationContext(), CarerHomepageActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);

        });

    alertDialog.show();
  }
}
