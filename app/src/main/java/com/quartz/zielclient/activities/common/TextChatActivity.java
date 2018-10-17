package com.quartz.zielclient.activities.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quartz.zielclient.R;
import com.quartz.zielclient.activities.carer.CarerMapsActivity;
import com.quartz.zielclient.adapters.MessageListAdapter;
import com.quartz.zielclient.channel.ChannelController;
import com.quartz.zielclient.channel.ChannelData;
import com.quartz.zielclient.channel.ChannelListener;
import com.quartz.zielclient.messages.Message;
import com.quartz.zielclient.messages.MessageFactory;
import com.quartz.zielclient.user.User;
import com.quartz.zielclient.user.UserFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Chat activity allows users to communicate with each other through messaging.
 */
public class TextChatActivity extends AppCompatActivity
    implements View.OnClickListener, ValueEventListener, ChannelListener {

  private ChannelData channel;
  private String currentUser;
  private Boolean isAssisted;
  private String carerName;
  private String assistedName;
  private DatabaseReference mRootRef;
  private StorageReference mImageStorage;

  // Recycler Views and Adapter for the text chat
  private RecyclerView mMessageRecycler;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView.Adapter mMessageListAdapter;
  private List<Message> messageList;

  // Graphical interfaces
  private Button sendMessage;
  private EditText chatInput;
  private Button mediaButton;

  private static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
  private static final int GALLERY_PICK = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_chat_message_list);

    // Checking whether currentUser is either assisted or carer
    isAssisted = getIntent().getBooleanExtra("isAssisted",false);


    // Fetching channel using handler
    String channelKey = getIntent().getStringExtra(getApplicationContext()
        .getString(R.string.channel_key));

    channel = ChannelController.retrieveChannel(channelKey, this);
    mRootRef = FirebaseDatabase.getInstance().getReference();
    mImageStorage = FirebaseStorage.getInstance().getReference();

    // Fetch the names of the users in the channel
    //carerName = channel.getCarerName();
    //assistedName = channel.getAssistedName();
    carerName = "Thanos";
    assistedName = "Despacito";

    // Chat using RecyclerView
    mMessageRecycler = findViewById(R.id.message_recyclerview);
    mLayoutManager = new LinearLayoutManager(this);
    mMessageRecycler.setLayoutManager(mLayoutManager);
    mMessageRecycler.setAdapter(new MessageListAdapter(this, new ArrayList<>(),
        false, "Carer", "Assisted"));

    // Getting the current user's username
    currentUser = FirebaseAuth.getInstance().getUid();

    // Initialise the graphical elements
    chatInput = findViewById(R.id.enter_chat_box);
    sendMessage = findViewById(R.id.button_chatbox_send);
    mediaButton = findViewById(R.id.button_media_send);
    sendMessage.setOnClickListener(this);

    /**
     * Adding a listener to see if the text input has changed
     */
    chatInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Intentionally Empty
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Intentionally Empty
      }

      // Make sure blank input does not get sent
      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
          sendMessage.setEnabled(true);
        } else {
          sendMessage.setEnabled(false);
        }
      }
    });
    
    // Set a listener on the media button to call requestMedia
    mediaButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view){
        // Request for permissions
        requestMedia();

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/* video/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
      }
    });

    // Greet User
    String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    Snackbar.make(mMessageRecycler, "Welcome to the Text Chat "
        + userName + "!", Snackbar.LENGTH_SHORT).show();

  }

  /**
   * Called to have the fragment instantiate its user interface view.
   * @param parent
   * @param name
   * @param context
   * @param attrs
   * @return
   */
  @Override
  public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
    return super.onCreateView(parent, name, context, attrs);
  }

  /**
   * Sorts the messages fetched from FireBase and assigns them to messageList to be displayed
   *
   * @param messagesInChat List of Messages already in the database
   */
  public void prepareData(List<Message> messagesInChat) {
    Collections.sort(messagesInChat);
    messageList = messagesInChat;
    // Creating a new Adapter to render the messages
    mMessageListAdapter = new MessageListAdapter(this, messageList,
        isAssisted, carerName, assistedName);
    mMessageRecycler.setAdapter(mMessageListAdapter);
  }

  /**
   * Update method to render messages as it updates
   */
  @Override
  public void dataChanged() {
    // Make sure the database of messages for the channel is not empty
    if (channel.getVideoCallStatus()) {
      onBackPressed();
    }

    // Convert Map of messages to List of messages
    Map<String, Message> messagesMap = channel.getMessages();
    List<Message> messages = new ArrayList<>(messagesMap.values());
    prepareData(messages);

    // Scroll to the bottom of the chat
    mMessageRecycler.scrollToPosition(messageList.size() - 1);
  }

  /**
   * Send message located in the input view into the channel database
   *
   * @param view The view input for the button, the messageText in this case
   */
  @Override
  public void onClick(View view) {
    Message messageToSend = MessageFactory.makeTextMessage(
        chatInput.getText().toString(), currentUser);
    channel.sendMessage(messageToSend);

    // Erasing the previously typed message
    if (chatInput != null) {
      chatInput.setText("");
    }
  }

  /**
   * Request media from the device and request for permission if it has already not done so.
   */
  public void requestMedia() {
    // If permission is not requested, request them.
    if(!checkPermissionForMedia()) {

    } else {
      ActivityCompat.requestPermissions(this,
          new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
          INTENT_REQUEST_CHOOSE_MEDIA);
    }
  }

  /**
   * Check if the permission for media to be sent is already requested
   * @return
   */
  private boolean checkPermissionForMedia() {
    int storage = ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    return storage == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Getting Image URIs
   * @param requestCode Request Code of the image request
   * @param resultCode Result Code of the image
   * @param data The intent being passed in
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == GALLERY_PICK && resultCode ==Activity.RESULT_OK) {
      Uri imageURL = data.getData();

      sendMediaMessageWithThumbnail(imageURL);
    }
  }

  /**
   * Send the message with specified thex`
   */
  private void sendMediaMessageWithThumbnail(Uri uri) {

    // Declaring the directories to store the files of the users
    final String currentUserReference = "messages/" + currentUser + "/" + isAssisted.toString();
    Boolean otherUser = !isAssisted;
    final String otherUserReference = "messages/" + currentUser + "/" + (otherUser.toString());

    String channelID = channel.getChannelKey();

    // Database reference to store the image path
    DatabaseReference userMessagePush = mRootRef.child("messages")
        .child(currentUser).child(isAssisted.toString());
    //String pushID = userMessagePush.getKey();

    //String directory = (isAssisted) ? isAssisted.toString(): otherUser.toString();

    // Adding the reference in which the image files are going to be pushed into Firebase
    StorageReference imageFilePath = mImageStorage.child("messages/" + channelID
        + messageList.size() + ".jpg");

    imageFilePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
      @Override
      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        String downloadURL = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

        // Performing null checks
        if(downloadURL != null) {
          // Send the image message
          Message messageToSend = MessageFactory.makeImageMessage(downloadURL, currentUser);
          channel.sendMessage(messageToSend);
        }
      }
    });


  }

  /**
   * Getter to indicate if the current user is Carer or Assisted
   * @return Boolean value if the user is assisted or not
   */
  public Boolean getAssisted() {
    return isAssisted;
  }

  /**
   * This method will be triggered in the event that this listener either failed at the server,
   * or is removed as a result of the security and Firebase rules.
   *
   * Documentation:  https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/
   * ValueEventListener.html
   *
   * @param databaseError A description of the error that occurred
   */
  @Override
  public void onCancelled(@NonNull DatabaseError databaseError) {

  }

  /**
   * This method will be called with a snapshot of the data at this location.
   * <p>
   * Documentation:  https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/
   * ValueEventListener.html
   *
   * @param dataSnapshot The current data at the location
   */
  @Override
  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

  }

  @Override
  public void onBackPressed(){
    // User could have come from either of these activities.
  MapsActivity.setPreviousActivityWasTextChat(true);
  CarerMapsActivity.setPreviousActivityWasTextChat(true);
  finish();

  }
}
