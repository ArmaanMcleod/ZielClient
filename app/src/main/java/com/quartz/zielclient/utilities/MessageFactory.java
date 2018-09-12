package com.quartz.zielclient.utilities;

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
}
