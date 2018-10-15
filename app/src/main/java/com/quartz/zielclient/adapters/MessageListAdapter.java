package com.quartz.zielclient.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.common.TextChatActivity;
import com.quartz.zielclient.messages.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Adapter Class used for adapting the Message objects into the Chat View.
 *
 * @author Wei How Ng
 */
public class MessageListAdapter extends RecyclerView.Adapter {

  // Constant for the flags used in the overridden method onCreateViewHolder
  private static final int VIEW_TYPE_MESSAGE_SENT = 1;
  private static final int VIEW_TYPE_MESSAGE_RECEIVED = 0;
  private static final int VIEW_TYPE_IMAGE_SENT = 11;
  private static final int VIEW_TYPE_IMAGE_RECEIVED = 10;
  private static final int VIEW_TYPE_VIDEO_SENT = 21;
  private static final int VIEW_TYPE_VIDEO_RECEIVED = 20;

  private Context mContext;
  private List<Message> messageList;
  private Boolean isAssisted;
  private Map<String, Uri> FileUriMap = new HashMap<>();
  private String carerName;
  private String assistedName;

  // Constructor
  public MessageListAdapter(Context context, List<Message> messageList, Boolean isAssisted,
                            String carerName, String assistedName) {
    mContext = context;
    this.messageList = messageList;
    this.isAssisted = isAssisted;
    this.carerName = carerName;
    this.assistedName = assistedName;
  }

  /**
   * Overridden method to inflate the right message to the respective view.
   *
   * @param viewGroup Parent
   * @param viewType  The type of message loaded
   * @return ViewHolder for the respective message type
   */
  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view;

    switch (viewType) {
      case VIEW_TYPE_MESSAGE_SENT:
        // If message a text message sent by the user
        view = LayoutInflater.from(viewGroup.getContext()).inflate
            (R.layout.message_sent, viewGroup, false);
        return new SentMessageHolder(view);

      case VIEW_TYPE_MESSAGE_RECEIVED:
        // If message is the one received by the user
        view = LayoutInflater.from(viewGroup.getContext()).inflate
            (R.layout.message_received, viewGroup, false);
        return new ReceivedMessageHolder(view);


      // TODO Make this not null or use an exception
      default:
        return null;
    }
  }

  // Binding the contents from the server to the front-end
  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    Message message = messageList.get(i);
    Uri tempFileUri = null;

    if (viewHolder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT) {

      // Bind sent message
      ((SentMessageHolder) viewHolder).bind(message);
    } else if (viewHolder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED) {

      // Bind received message
      ((ReceivedMessageHolder) viewHolder).bind(message);
    }
  }

  /**
   * Getting count of current number of messagess
   *
   * @return Size of messageList as an int
   */
  @Override
  public int getItemCount() {
    return messageList.size();
  }

  /**
   * Check if the message is one that is being sent by the current user.
   *
   * @param position The position of the message being sent currently
   * @return The type of message being sent in int
   */
  @Override
  public int getItemViewType(int position) {
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
   * Triple checks that the message is valid.
   *
   * @param message The message object being checked.
   * @return Whether the message has failed or not.
   */
  public boolean isFailedMessage(Message message) {
    // Check if message has a sent time and value
    if ((message.getMessageTime() > 0) && !message.getMessageValue().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Holder class for the received messages
   */
  private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    // Message Attributes
    // TODO Implement Different Message Types
    // Message.MessageType messageType;
    TextView messageText;
    TextView timeStamp;
    TextView userName;
    ImageView profilePicture;
    String sender;

    ReceivedMessageHolder(View itemView) {
      super(itemView);
      messageText = itemView.findViewById(R.id.text_message_body);
      timeStamp = itemView.findViewById(R.id.text_message_time);
      userName = itemView.findViewById(R.id.text_message_name);
      profilePicture = (ImageView) itemView.findViewById(R.id.image_message_profile);

      // TODO Refactor this ugly code
      // Checking whether the received message belongs to the carer or assisted
      if (isAssisted) {

        // Null Checks and assign name of carer
        sender = (carerName.isEmpty()) ? "Carer" :  carerName;
      } else {

        // Null Checks and assign name of assisted
        sender = (assistedName.isEmpty()) ? "Assisted" : assistedName;
      }

      profilePicture = itemView.findViewById(R.id.image_message_profile);
    }

    // Bind Method
    // TODO Add profile picture support
    void bind(Message message) {
      messageText.setText(message.getMessageValue());
      String timeString = new SimpleDateFormat("h:mm a").format(message.getMessageTime());
      timeStamp.setText(timeString);
      userName.setText(sender);
      // TODO
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
    TextView messageText;
    TextView timeStamp;
    TextView userName;

    SentMessageHolder(View itemView) {
      super(itemView);
      messageText = itemView.findViewById(R.id.text_message_body);
      timeStamp = itemView.findViewById(R.id.text_message_time);
      userName = itemView.findViewById(R.id.text_message_name);
    }

    // Bind Method
    void bind(Message message) {
      messageText.setText(message.getMessageValue());
      String timeString = new SimpleDateFormat("h:mm a").format(message.getMessageTime());
      timeStamp.setText(timeString);
    }
  }
}
  /**
   * A ViewHolder for file messages that are images.
   * Displays only the image thumbnail.
   */
/*
  private class SentImageHolder extends RecyclerView.ViewHolder {
    TextView timeStamp;
    ImageView fileThumbnailImage;
    CircleProgressBar circleProgressBar;

    public SentImageHolder(View itemView) {
      super(itemView);

      timeStamp = (TextView) itemView.findViewById(R.id.text_group_chat_time);
      fileThumbnailImage = (ImageView) itemView.findViewById(R.id.image_group_chat_file_thumbnail);
      circleProgressBar = (CircleProgressBar) itemView.findViewById(R.id.circle_progress);
    }

    void bind(Message message){

      if (isFailedMessage(message)) {
        // If the message has appeared to be a failed message
        circleProgressBar.setVisibility(View.GONE);
        messageList.remove(message);

      } else {
        // If the message sends without problems
        circleProgressBar.setVisibility(View.VISIBLE);
        String timeString = new SimpleDateFormat("h:mm a").format(message.getMessageTime());
        timeStamp.setText(timeString);
      }

      // Checking the Message URI
      if (tempFileMessageUri != null) {
        ImageUtils.displayImageFromUrl(context, tempFileMessageUri.toString(), fileThumbnailImage, null);
      } else {
        // Get thumbnails from FileMessage
        ArrayList<FileMessage.Thumbnail> thumbnails = (ArrayList<FileMessage.Thumbnail>) message.getThumbnails();

        // If thumbnails exist, get smallest (first) thumbnail and display it in the message
        if (thumbnails.size() > 0) {
          if (message.getType().toLowerCase().contains("gif")) {
            ImageUtils.displayGifImageFromUrl(context, message.getUrl(), fileThumbnailImage, thumbnails.get(0).getUrl(), fileThumbnailImage.getDrawable());
          } else {
            ImageUtils.displayImageFromUrl(context, thumbnails.get(0).getUrl(), fileThumbnailImage, fileThumbnailImage.getDrawable());
          }
        } else {
          if (message.getType().toLowerCase().contains("gif")) {
            ImageUtils.displayGifImageFromUrl(context, message.getUrl(), fileThumbnailImage, (String) null, fileThumbnailImage.getDrawable());
          } else {
            ImageUtils.displayImageFromUrl(context, message.getUrl(), fileThumbnailImage, fileThumbnailImage.getDrawable());
          }
        }
      }

      if (listener != null) {
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onFileMessageItemClick(message);
          }
        });
      }
    }
  }

  private class OtherImageFileMessageHolder extends RecyclerView.ViewHolder {

    TextView timeText, nicknameText, readReceiptText, dateText;
    ImageView profileImage, fileThumbnailImage;

    public OtherImageFileMessageHolder(View itemView) {
      super(itemView);

      timeText = (TextView) itemView.findViewById(R.id.text_group_chat_time);
      nicknameText = (TextView) itemView.findViewById(R.id.text_group_chat_nickname);
      fileThumbnailImage = (ImageView) itemView.findViewById(R.id.image_group_chat_file_thumbnail);
      profileImage = (ImageView) itemView.findViewById(R.id.image_group_chat_profile);
      readReceiptText = (TextView) itemView.findViewById(R.id.text_group_chat_read_receipt);
      dateText = (TextView) itemView.findViewById(R.id.text_group_chat_date);
    }

    void bind(Context context, final FileMessage message, GroupChannel channel, boolean isNewDay, boolean isContinuous, final OnItemClickListener listener) {
      timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

      // Since setChannel is set slightly after adapter is created, check if null.
      if (channel != null) {
        int readReceipt = channel.getReadReceipt(message);
        if (readReceipt > 0) {
          readReceiptText.setVisibility(View.VISIBLE);
          readReceiptText.setText(String.valueOf(readReceipt));
        } else {
          readReceiptText.setVisibility(View.INVISIBLE);
        }
      }

      // Show the date if the message was sent on a different date than the previous message.
      if (isNewDay) {
        dateText.setVisibility(View.VISIBLE);
        dateText.setText(DateUtils.formatDate(message.getCreatedAt()));
      } else {
        dateText.setVisibility(View.GONE);
      }

      // Hide profile image and nickname if the previous message was also sent by current sender.
      if (isContinuous) {
        profileImage.setVisibility(View.INVISIBLE);
        nicknameText.setVisibility(View.GONE);
      } else {
        profileImage.setVisibility(View.VISIBLE);
        ImageUtils.displayRoundImageFromUrl(context, message.getSender().getProfileUrl(), profileImage);

        nicknameText.setVisibility(View.VISIBLE);
        nicknameText.setText(message.getSender().getNickname());
      }

      // Get thumbnails from FileMessage
      ArrayList<FileMessage.Thumbnail> thumbnails = (ArrayList<FileMessage.Thumbnail>) message.getThumbnails();

      // If thumbnails exist, get smallest (first) thumbnail and display it in the message
      if (thumbnails.size() > 0) {
        if (message.getType().toLowerCase().contains("gif")) {
          ImageUtils.displayGifImageFromUrl(context, message.getUrl(), fileThumbnailImage, thumbnails.get(0).getUrl(), fileThumbnailImage.getDrawable());
        } else {
          ImageUtils.displayImageFromUrl(context, thumbnails.get(0).getUrl(), fileThumbnailImage, fileThumbnailImage.getDrawable());
        }
      } else {
        if (message.getType().toLowerCase().contains("gif")) {
          ImageUtils.displayGifImageFromUrl(context, message.getUrl(), fileThumbnailImage, (String) null, fileThumbnailImage.getDrawable());
        } else {
          ImageUtils.displayImageFromUrl(context, message.getUrl(), fileThumbnailImage, fileThumbnailImage.getDrawable());
        }
      }

      if (listener != null) {
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onFileMessageItemClick(message);
          }
        });
      }
    }
  }

  /**
   * A ViewHolder for file messages that are videos.
   * Displays only the video thumbnail.
   */
/*
  private class MyVideoFileMessageHolder extends RecyclerView.ViewHolder {
    TextView timeText, readReceiptText, dateText;
    ImageView fileThumbnailImage;
    CircleProgressBar circleProgressBar;

    public MyVideoFileMessageHolder(View itemView) {
      super(itemView);

      timeText = (TextView) itemView.findViewById(R.id.text_group_chat_time);
      fileThumbnailImage = (ImageView) itemView.findViewById(R.id.image_group_chat_file_thumbnail);
      readReceiptText = (TextView) itemView.findViewById(R.id.text_group_chat_read_receipt);
      circleProgressBar = (CircleProgressBar) itemView.findViewById(R.id.circle_progress);
      dateText = (TextView) itemView.findViewById(R.id.text_group_chat_date);
    }

    void bind(Context context, final FileMessage message, GroupChannel channel, boolean isNewDay, boolean isTempMessage, boolean isFailedMessage, Uri tempFileMessageUri, final OnItemClickListener listener) {
      timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

      if (isFailedMessage) {
        readReceiptText.setText(R.string.message_failed);
        readReceiptText.setVisibility(View.VISIBLE);

        circleProgressBar.setVisibility(View.GONE);
        mFileMessageMap.remove(message);
      } else if (isTempMessage) {
        readReceiptText.setText(R.string.message_sending);
        readReceiptText.setVisibility(View.GONE);

        circleProgressBar.setVisibility(View.VISIBLE);
        mFileMessageMap.put(message, circleProgressBar);
      } else {
        circleProgressBar.setVisibility(View.GONE);
        mFileMessageMap.remove(message);

        // Since setChannel is set slightly after adapter is created, check if null.
        if (channel != null) {
          int readReceipt = channel.getReadReceipt(message);
          if (readReceipt > 0) {
            readReceiptText.setVisibility(View.VISIBLE);
            readReceiptText.setText(String.valueOf(readReceipt));
          } else {
            readReceiptText.setVisibility(View.INVISIBLE);
          }
        }
      }

      // Show the date if the message was sent on a different date than the previous message.
      if (isNewDay) {
        dateText.setVisibility(View.VISIBLE);
        dateText.setText(DateUtils.formatDate(message.getCreatedAt()));
      } else {
        dateText.setVisibility(View.GONE);
      }

      if (isTempMessage && tempFileMessageUri != null) {
        ImageUtils.displayImageFromUrl(context, tempFileMessageUri.toString(), fileThumbnailImage, null);
      } else {
        // Get thumbnails from FileMessage
        ArrayList<FileMessage.Thumbnail> thumbnails = (ArrayList<FileMessage.Thumbnail>) message.getThumbnails();

        // If thumbnails exist, get smallest (first) thumbnail and display it in the message
        if (thumbnails.size() > 0) {
          ImageUtils.displayImageFromUrl(context, thumbnails.get(0).getUrl(), fileThumbnailImage, fileThumbnailImage.getDrawable());
        }
      }

      if (listener != null) {
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onFileMessageItemClick(message);
          }
        });
      }
    }
  }

  private class OtherVideoFileMessageHolder extends RecyclerView.ViewHolder {

    TextView timeText, nicknameText, readReceiptText, dateText;
    ImageView profileImage, fileThumbnailImage;

    public OtherVideoFileMessageHolder(View itemView) {
      super(itemView);

      timeText = (TextView) itemView.findViewById(R.id.text_group_chat_time);
      nicknameText = (TextView) itemView.findViewById(R.id.text_group_chat_nickname);
      fileThumbnailImage = (ImageView) itemView.findViewById(R.id.image_group_chat_file_thumbnail);
      profileImage = (ImageView) itemView.findViewById(R.id.image_group_chat_profile);
      readReceiptText = (TextView) itemView.findViewById(R.id.text_group_chat_read_receipt);
      dateText = (TextView) itemView.findViewById(R.id.text_group_chat_date);
    }

    void bind(Context context, final FileMessage message, GroupChannel channel, boolean isNewDay, boolean isContinuous, final OnItemClickListener listener) {
      timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

      // Since setChannel is set slightly after adapter is created, check if null.
      if (channel != null) {
        int readReceipt = channel.getReadReceipt(message);
        if (readReceipt > 0) {
          readReceiptText.setVisibility(View.VISIBLE);
          readReceiptText.setText(String.valueOf(readReceipt));
        } else {
          readReceiptText.setVisibility(View.INVISIBLE);
        }
      }

      // Show the date if the message was sent on a different date than the previous message.
      if (isNewDay) {
        dateText.setVisibility(View.VISIBLE);
        dateText.setText(DateUtils.formatDate(message.getCreatedAt()));
      } else {
        dateText.setVisibility(View.GONE);
      }

      // Hide profile image and nickname if the previous message was also sent by current sender.
      if (isContinuous) {
        profileImage.setVisibility(View.INVISIBLE);
        nicknameText.setVisibility(View.GONE);
      } else {
        profileImage.setVisibility(View.VISIBLE);
        ImageUtils.displayRoundImageFromUrl(context, message.getSender().getProfileUrl(), profileImage);

        nicknameText.setVisibility(View.VISIBLE);
        nicknameText.setText(message.getSender().getNickname());
      }

      // Get thumbnails from FileMessage
      ArrayList<FileMessage.Thumbnail> thumbnails = (ArrayList<FileMessage.Thumbnail>) message.getThumbnails();

      // If thumbnails exist, get smallest (first) thumbnail and display it in the message
      if (thumbnails.size() > 0) {
        ImageUtils.displayImageFromUrl(context, thumbnails.get(0).getUrl(), fileThumbnailImage, fileThumbnailImage.getDrawable());
      }

      if (listener != null) {
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onFileMessageItemClick(message);
          }
        });
      }
    }
  }
}

*/


