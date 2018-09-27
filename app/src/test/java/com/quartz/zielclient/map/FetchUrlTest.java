package com.quartz.zielclient.map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

public class FetchUrlTest {

  private String[] testUrls;

  @Before
  public void setUp() {

    // Test API urls from https://jsonplaceholder.typicode.com/
    // Fake Online REST API
    testUrls = new String[] {
        "https://jsonplaceholder.typicode.com/posts",
        "https://jsonplaceholder.typicode.com/comments",
        "https://jsonplaceholder.typicode.com/albums",
        "https://jsonplaceholder.typicode.com/photos",
        "https://jsonplaceholder.typicode.com/todos",
        "https://jsonplaceholder.typicode.com/users"
    };
  }

  @After
  public void tearDown() {
    testUrls = null;
  }

  @Test
  public void testUrls() {
    for (String strUrl : testUrls) {
      try {
        URL url = new URL(strUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        assertEquals(HttpURLConnection.HTTP_OK, urlConnection.getResponseCode());
      } catch (IOException e) {
        System.err.println("Error creating HTTP connection");
        e.printStackTrace();
      }
    }
  }

  @Test
  public void downloadUrls() {
    for (String url : testUrls) {
      try {
        String result = FetchUrl.downloadUrl(url);
        assertFalse(result.isEmpty());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}