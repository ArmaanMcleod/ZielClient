package com.quartz.zielclient.activities.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.quartz.zielclient.R;
import com.quartz.zielclient.adapters.MessageListAdapter;
import com.quartz.zielclient.channel.ChannelController;
import com.quartz.zielclient.channel.ChannelData;
import com.quartz.zielclient.channel.ChannelListener;
import com.quartz.zielclient.messages.Message;
import com.quartz.zielclient.messages.MessageFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Chat activity allows users to communicate with eachother through messaging
 * This activity is currently unstyled.
 */
public class TextChatActivity extends AppCompatActivity
    implements View.OnClickListener, ValueEventListener, ChannelListener {

  private ChannelData channel;
  private String currentUser;

  // Recycler Views and Adapter for the text chat
  private RecyclerView mMessageRecycler;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView.Adapter mMessageListAdapter;
  private List<Message> messageList;

  // Graphical interfaces
  private Button sendMessage;
  private EditText chatInput;
  private Button mediaButton;

  private static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_chat_message_list);

    // Fetching channel using handler
    String channelKey = getIntent().getStringExtra(getApplicationContext()
        .getString(R.string.channel_key));

    channel = ChannelController.retrieveChannel(channelKey, this);

    // Fetch the pre-existing messages from the database first
    // Convert Map of messages to List of messages
    /*
    Map<String, Message> messagesMap = channel.getMessages();
    List<Message> messages = new ArrayList<Message>(messagesMap.values());
    prepareData(messages);
    */

    // Chat using RecyclerView
    mMessageRecycler = findViewById(R.id.message_recyclerview);
    mLayoutManager = new LinearLayoutManager(this);
    mMessageRecycler.setLayoutManager(mLayoutManager);
    mMessageRecycler.setAdapter(new MessageListAdapter(this, new ArrayList<>()));

    // Getting the current user's username
    currentUser = FirebaseAuth.getInstance().getUid();

    // Initialise the graphical elements
    chatInput = findViewById(R.id.enter_chat_box);
    sendMessage = findViewById(R.id.button_chatbox_send);
    // TODO initialise media button
    sendMessage.setOnClickListener(this);

    /**
     * Adding a listener to see if the text input has changed
     */
    chatInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Intentionally Empty
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Intentionally Empty
      }

      // Make sure blank input does not get sent
      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
          sendMessage.setEnabled(true);
        } else {
          sendMessage.setEnabled(false);
        }
      }
    });
    
    /*
    mediaButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        requestMedia();
      }
    });
    */

    // Greet User
    Snackbar.make(mMessageRecycler, "Welcome to the Text Chat "
        + currentUser + "!", Snackbar.LENGTH_SHORT).show();

  }

  @Override
  public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
    return super.onCreateView(parent, name, context, attrs);
  }

  /**
   * Sorts the messages fetched from FireBase and assigns them to messageList to be displayed
   *
   * @param messagesInChat List of Messages already in the database
   */
  public void prepareData(List<Message> messagesInChat) {
    Collections.sort(messagesInChat);
    messageList = messagesInChat;
    // Creating a new Adapter to render the messages
    mMessageListAdapter = new MessageListAdapter(this, messageList);
    mMessageRecycler.setAdapter(mMessageListAdapter);
  }

  /**
   * Update method to render messages as it updates
   */
  @Override
  public void dataChanged() {
    // Make sure the database of messages for the channel is not empty
    if (channel.getVideoCallStatus()) {
      onBackPressed();
    }

    // Convert Map of messages to List of messages
    Map<String, Message> messagesMap = channel.getMessages();
    List<Message> messages = new ArrayList<>(messagesMap.values());
    prepareData(messages);

    // Scroll to the bottom of the chat
    mMessageRecycler.scrollToPosition(messageList.size() - 1);
  }

  /**
   * Send message located in the input view into the channel database
   *
   * @param view The view input for the button, the messageText in this case
   */
  @Override
  public void onClick(View view) {
    Message messageToSend = MessageFactory.makeTextMessage(
        chatInput.getText().toString(), currentUser);
    channel.sendMessage(messageToSend);

    mMessageRecycler.setAdapter(mMessageListAdapter);
  }

  /**
   * Request media from the device and request for permission if it has already not done so.
   */
  public void requestMedia() {
    // If permission is not requested, request them.
    if(!checkPermissionForMedia()) {

    } else {
      ActivityCompat.requestPermissions(this,
          new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
          INTENT_REQUEST_CHOOSE_MEDIA);
    }
  }

  /**
   * Check if the permission for media to be sent is already requested
   * @return
   */
  private boolean checkPermissionForMedia() {
    int storage = ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    return storage == PackageManager.PERMISSION_GRANTED;
  }

  // TODO
  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {

  }

  // TODO
  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

  }
}
