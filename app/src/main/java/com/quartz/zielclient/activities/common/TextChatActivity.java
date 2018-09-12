package com.quartz.zielclient.activities.common;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.utilities.Message;
import com.quartz.zielclient.utilities.MessageFactory;
import com.quartz.zielclient.utilities.channel.Channel;
import com.quartz.zielclient.utilities.channel.ChannelHandler;
import com.quartz.zielclient.utilities.channel.ChannelListener;

/**
 * Chat activity allows users to communicate with eachother through messaging
 * This activity is currently unstyled.
 */
public class TextChatActivity extends AppCompatActivity implements ChannelListener, View.OnClickListener {

  // TODO handle channels.
  private static final String DEBUG_ID = "90a2c51d-4d9a-4d15-af8e-9639ff472231";

  // temporary for debugging will become a dynamic channel
  private final Channel channel = ChannelHandler.retrieveChannel(DEBUG_ID, this);
  private TextView chatOutput;
  private TextInputEditText chatInput;
  private Button sendButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_chat);
    // initialize graphical elements
    chatOutput = findViewById(R.id.chatOutput);
    chatInput = findViewById(R.id.chatInput);
    sendButton = findViewById(R.id.sendButton);
    sendButton.setOnClickListener(this);
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
   *
   * @param view
   */
  @Override
  public void onClick(View view) {
    Message messageToSend = MessageFactory.makeTextMessage(chatInput.getText().toString());
    channel.sendMessage(messageToSend);

  }
}
