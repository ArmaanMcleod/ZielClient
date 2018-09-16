package com.quartz.zielclient.messages;

import android.support.annotation.NonNull;

import com.quartz.zielclient.messages.Message;

import java.util.Map;

/**
 * This builder creates the messages of various types
 *
 * @author Bilal Shehata
 */
public final class MessageFactory {

  private MessageFactory() {
  }

  /**
   * Create text message
   *
   * @param text The message's text.
   * @return The
   */
  public static Message makeTextMessage(String text, String userName) {
    return new Message(Message.MessageType.TEXT, text, userName);
  }

  public static Message makeTextMessage(String text, String userName, long timestamp) {
    return new Message(Message.MessageType.TEXT, text, userName, timestamp);
  }

  public static Message makeMessage(@NonNull Map<String, Object> messageData) {
    String messageText = (String) messageData.get("messageText");
    String messageSender = (String) messageData.get("username");
    // Box it to avoid null pointer exceptions
    Long messageTime = (Long) messageData.get("messageTime");
    return makeTextMessage(messageText, messageSender, messageTime);
  }
}
