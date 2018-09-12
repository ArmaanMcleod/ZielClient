package com.quartz.zielclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.utilities.Message;
import java.util.List;

/**
 * Adapter Class used for adapting the Message objects into the Chat View.
 */
public class MessageListAdapter {
  private Context mContext;
  private List<Message> messageList;

  public MessageListAdapter(Context context, List<Message> messageList) {
    mContext = context;
    this.messageList = messageList;
  }

  /**
   * Holder class for the received messages
   */
  private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    // Message Attributes
    // TODO Implement Different Message Types
    // Message.MessageType messageType;
    TextView messageText, timeStamp, userName;
    ImageView profilePicture;

    ReceivedMessageHolder(View itemView) {
      super(itemView);
      messageText = itemView.findViewById(R.id.text_message_body);
      timeStamp = itemView.findViewById(R.id.text_message_time);
      userName = itemView.findViewById(R.id.text_message_name);
      profilePicture = (ImageView) itemView.findViewById(R.id.image_message_profile);
    }

    // Bind Method
    void bind( ) {
      
    }
  }
}
