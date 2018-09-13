package com.quartz.zielclient.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.utilities.Message;

import java.util.Date;
import java.util.List;

/**
 * Adapter Class used for adapting the Message objects into the Chat View.
 */
public class MessageListAdapter extends RecyclerView.Adapter{
  private Context mContext;
  private List<Message> messageList;

  // Constructor
  public MessageListAdapter(Context context, List<Message> messageList) {
    mContext = context;
    this.messageList = messageList;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return null;
  }

  // Binding the contents from the server to the front-end
  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    Message message = messageList.get(i);

    // Fetching the details of each message

  }

  @Override
  public int getItemCount() {
    return messageList.size();
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
    // TODO Add profile picture support
    void bind(Message message) {
      messageText.setText(message.getMessageValue());
      Date date = new Date(message.getMessageTime());
      timeStamp.setText(DateUtils.formatDateTime(mContext, message.getMessageTime(), 0));
      // ADD PROFILE PICTURE BIND HERE
    }
  }

  /**
   * Holder class for the sent messages
   */
  private class SentMessageHolder extends RecyclerView.ViewHolder {
    // Message Attributes
    // TODO Implement Different Message Types
    // Message.MessageType messageType;
    TextView messageText, timeStamp, userName;

    SentMessageHolder(View itemView) {
      super(itemView);
      messageText = itemView.findViewById(R.id.text_message_body);
      timeStamp = itemView.findViewById(R.id.text_message_time);
      userName = itemView.findViewById(R.id.text_message_name);
    }

    // Bind Method
    void bind(Message message) {
      messageText.setText(message.getMessageValue());
      timeStamp.setText(DateUtils.formatDateTime(mContext, message.getMessageTime(), 0));
    }
  }
}


