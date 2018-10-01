package com.quartz.zielclient.activities.common;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
public class TextChatActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, ChannelListener {

  private ChannelData channel;
  private TextView chatOutput;
  private EditText chatInput;
  private String currentUser;

  // Recycler Views and Adapter for the text chat
  private RecyclerView mMessageRecycler;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView.Adapter mMessageListAdapter;
  private List<Message> messageList;

  // Graphical interfaces
  private Button sendMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_chat_message_list);

    // Fetching channel using handler
    String channelKey = getIntent().getStringExtra(getApplicationContext()
        .getString(R.string.channel_key));

    channel = ChannelController.retrieveChannel(channelKey, this);


    // Chat using RecyclerView
    mMessageRecycler = findViewById(R.id.message_recyclerview);
    mMessageListAdapter = new MessageListAdapter(this, messageList);
    mLayoutManager = new LinearLayoutManager(this);
    mMessageRecycler.setLayoutManager(mLayoutManager);

    // Getting the current user's username
    currentUser = FirebaseAuth.getInstance().getUid();

    // Initialise the graphical elements
    chatInput = findViewById(R.id.enter_chat_box);
    sendMessage = findViewById(R.id.button_chatbox_send);
    sendMessage.setOnClickListener(this);

    // Greet User
    Snackbar.make(mMessageRecycler, "Welcome to the Text Chat " + currentUser, Snackbar.LENGTH_SHORT);
    /*
    // initialize graphical elements
    chatInput = findViewById(R.id.chatInput);
    Button sendButton = findViewById(R.id.sendButton);
    sendButton.setOnClickListener(this);
    */
  }

  /**
   * Sorts the messages fetched from FireBase and assigns them to messageList to be displayed
   * @param messagesInChat List of Messages already in the database
   */
  public void prepareData(List<Message> messagesInChat) {
    Collections.sort(messagesInChat);
    messageList = messagesInChat;
  }

  /**
   * Rendering the Message List whenever there is a new message
   */
  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    System.out.println("hello");
    // Make sure the database of messages for the channel is not empty
    if (channel.getMessages() != null) {
      // Convert Map of messages to List of messages
      Map<String, Message> messagesMap = channel.getMessages();
      List<Message> messages = new ArrayList<Message>(messagesMap.values());

      prepareData(messages);

      // Creating a new Adapter to render the messages
      mMessageListAdapter = new MessageListAdapter(this, messageList);
      mMessageRecycler.setAdapter(mMessageListAdapter);
    }
  }

  /**
   * Send message located in the input view into the channel database
   * @param view The view input for the button, the messageText in this case
   */
  @Override
  public void onClick(View view) {
    Message messageToSend = MessageFactory.makeTextMessage(chatInput.getText().toString(), currentUser);
    channel.sendMessage(messageToSend);
  }

  // TODO
  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {

  }

  // TODO
  @Override
  public void dataChanged() {

  }
}
