package com.quartz.zielclient.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.quartz.zielclient.R;
import com.quartz.zielclient.messages.Message;

import java.util.Date;
import java.util.List;


/**
 * Adapter Class used for adapting the Message objects into the Chat View.
 */
public class MessageListAdapter extends RecyclerView.Adapter{

  // Constant for the flags used in the overridden method onCreateViewHolder
  private static final int VIEW_TYPE_MESSAGE_SENT = 1;
  private static final int VIEW_TYPE_MESSAGE_RECEIVED = 0;

  private Context mContext;
  private List<Message> messageList;

  // Constructor
  public MessageListAdapter(Context context, List<Message> messageList) {
    mContext = context;
    this.messageList = messageList;
  }

  /**
   * Overridden method to inflate the right message to the respective view.
   * @param viewGroup Parent
   * @param viewType The type of message loaded
   * @return ViewHolder for the respective message type
   */
  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view;

    // If message is the one sent by the user
    if (viewType == VIEW_TYPE_MESSAGE_SENT) {
      view = LayoutInflater.from(viewGroup.getContext()).inflate
          (R.layout.message_sent, viewGroup, false);

      return new SentMessageHolder(view);
    } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {

      // If message is the one received by the user
      view = LayoutInflater.from(viewGroup.getContext()).inflate
          (R.layout.message_received,viewGroup,false);

      return new ReceivedMessageHolder(view);
    }

    // TODO Make this not null or use an exception
    return null;
  }

  // Binding the contents from the server to the front-end
  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    Message message = messageList.get(i);

    if (viewHolder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT) {

      // Bind sent message
      ((SentMessageHolder) viewHolder).bind(message);
    } else if (viewHolder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED) {

      // Bind received message
      ((ReceivedMessageHolder) viewHolder).bind(message);
    }

    else {
      System.out.println("AAAAAAAAAAAAAAAAAA");
    }
  }

  /**
   * Getting count of current number of messagess
   * @return Size of messageList as an int
   */
  @Override
  public int getItemCount() {
    return messageList.size();
  }

  /**
   * Check if the message is one that is being sent by the current user.
   * @param position The position of the message being sent currently
   * @return The type of message being sent in int
   */
  @Override
  public int getItemViewType (int position) {
    Message message = messageList.get(position);

    // Checking current message's sender's ID against current user's ID
    if (message.getUserName().equals(FirebaseAuth.getInstance().getUid())) {
      // If current user is the sender of message
      return VIEW_TYPE_MESSAGE_SENT;
    } else {
      // Another user sent the message
      return VIEW_TYPE_MESSAGE_RECEIVED;
    }
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


