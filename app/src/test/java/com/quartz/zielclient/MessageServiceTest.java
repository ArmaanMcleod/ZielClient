package com.quartz.zielclient;

import com.quartz.zielclient.messages.Message;
import com.quartz.zielclient.messages.MessageService;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MessageServiceTest {

  private Map<String, Map<String, Object>> testMessageData;

  @Before
  public void setup() {
    Map<String, Map<String, Object>> test1 = new HashMap<>();
    Map<String, Object> test1Message = new HashMap<>();
    test1Message.put("username", "wei");
    test1Message.put("messageText", "this code sucks");
    test1Message.put("messageType", "TEXT");
    test1Message.put("messageTime", 12345L);
    test1.put("testMessage", test1Message);
    testMessageData = test1;
  }

  @Test
  public void deserialise_test() {
    Map<String, Message> testMessage = MessageService.deserialiseMessages(testMessageData);
    assertEquals("this code sucks", testMessage.get("testMessage").getMessageValue());
  }
}
