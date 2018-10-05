package com.quartz.zielclient.messages;

import android.support.annotation.NonNull;

import com.quartz.zielclient.messages.Message;

import java.util.Map;

/**
 * This builder creates the messages of various types
 *
 * @author Wei How Ng
 */
public final class MessageFactory {

  private MessageFactory() {
  }

  /**
   * Create text message when generating the text message
   *
   * @param text The message's text.
   * @param userName The userName of the sender of this message.
   * @return The newly generated text message.
   */
  @NonNull
  public static Message makeTextMessage(String text, String userName) {
    return new Message(Message.MessageType.TEXT, text, userName);
  }

  /**
   * Create text message object for fetching the text messages from Firebase.
   *
   * @param text The message's text.
   * @param userName The userName of the sender of this message.
   * @param timestamp The recorded timestamp of the stored message.
   * @return The newly generated text message.
   */
  public static Message makeTextMessage(String text, String userName, long timestamp) {
    return new Message(Message.MessageType.TEXT, text, userName, timestamp);
  }

  public static Message makeMessage(@NonNull Map<String, Object> messageData) {
    String messageText = (String) messageData.get("messageValue");
    String messageSender = (String) messageData.get("userName");
    // Box it to avoid null pointer exceptions
    Long messageTime = (Long) messageData.get("messageTime");
    return makeTextMessage(messageText, messageSender, messageTime);
  }
}
