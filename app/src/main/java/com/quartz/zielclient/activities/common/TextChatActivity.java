package com.quartz.zielclient.activities.common;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.adapters.MessageListAdapter;
import com.quartz.zielclient.channel.ChannelController;
import com.quartz.zielclient.channel.ChannelData;
import com.quartz.zielclient.channel.ChannelListener;
import com.quartz.zielclient.messages.Message;
import com.quartz.zielclient.messages.MessageFactory;

import java.util.List;
import java.util.Objects;

/**
 * Chat activity allows users to communicate with eachother through messaging
 * This activity is currently unstyled.
 */
public class TextChatActivity extends AppCompatActivity implements ChannelListener, View.OnClickListener {

  // TODO handle channels.
  private static final String DEBUG_ID = "90a2c51d-4d9a-4d15-af8e-9639ff472231";

  // temporary for debugging will become a dynamic channel
  private final ChannelData channel = ChannelController.retrieveChannel(DEBUG_ID, this);
  private TextView chatOutput;
  private TextInputEditText chatInput;

  // Recycler Views and Adapter for the text chat
  private RecyclerView mMessageRecycler;
  private MessageListAdapter mMessageListAdapter;
  private List<Message> messageList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_chat);

    // Chat using RecyclerView
    mMessageRecycler = findViewById(R.id.message_recyclerview);
    mMessageListAdapter = new MessageListAdapter(this, messageList);
    mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

    // Initialise the graphical elements
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

  @Override
  public void dataChanged() {
    if (channel.getMessages() != null) {
      // this is completely unstylised representation of the messages
      chatOutput.setText(channel.getMessages().toString());
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
   * Send message located in the input view
   * TODO: Add the sender/ receiver's username into this instead of the hardcoded string
   * @param view
   */
  @Override
  public void onClick(View view) {
    Message messageToSend = MessageFactory.makeTextMessage(chatInput.getText().toString(), "Name");
    channel.sendMessage(messageToSend);
  }
}
