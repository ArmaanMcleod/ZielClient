package com.quartz.zielclient.activities.common;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Objects;

/**
 * Chat activity allows users to communicate with eachother through messaging
 * This activity is currently unstyled.
 */
public class TextChatActivity extends AppCompatActivity implements ChannelListener, View.OnClickListener {


  // temporary for debugging will become a dynamic channel
  private ChannelData channel;
  private TextView chatOutput;
  private EditText chatInput;
  private String currentUser;

  // Recycler Views and Adapter for the text chat
  private RecyclerView mMessageRecycler;
  private MessageListAdapter mMessageListAdapter;
  private List<Message> messageList;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_chat_message_list);

    // Chat using RecyclerView
    mMessageRecycler = findViewById(R.id.message_recyclerview);
    mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

    // Fetching channel using handler
    String channelKey = "channel-1";
        /*getIntent().getStringExtra(getApplicationContext()
        .getString(R.string.channel_key));*/

    channel = ChannelController.retrieveChannel(channelKey, this);

    // Getting the current user's username
    currentUser = FirebaseAuth.getInstance().getUid();

    // Initialise the graphical elements
    chatInput = findViewById(R.id.enter_chat_box);
    Button sendMessage = findViewById(R.id.button_chatbox_send);
    sendMessage.setOnClickListener(this);

    /*
    // initialize graphical elements
    chatOutput = findViewById(R.id.chatOutput);
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
  public void dataChanged() {
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

  @Override
  public String getAssistedId() {
    return null;
  }

  @Override
  public String getCarerId() {
    return null;
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
}
