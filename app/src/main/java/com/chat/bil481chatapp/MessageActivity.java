package com.chat.bil481chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    String activeUser = "";

    ArrayList<Message> messages = new ArrayList<>();
    ChatAdapter chatAdapter;
    RecyclerView rvChat;
    ImageButton btSend;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        btSend = findViewById(R.id.btSend);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText chatEditText = findViewById(R.id.etMessage);
                final String messageContent = chatEditText.getText().toString();

                ParseObject message = ParseObject.create("Message");
                message.put("sender", ParseUser.getCurrentUser().getUsername());
                message.put("recipient",activeUser);
                message.put("message",messageContent);


                chatEditText.setText("");

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){

                            Message typedMessage = new Message();
                            typedMessage.setBody(messageContent);
                            messages.add(typedMessage);

                            chatAdapter.notifyDataSetChanged();
                        }
                    }
                });


            }
        });

        Intent intent = getIntent();

        activeUser = intent.getStringExtra("username");
        setTitle("Chat with "+ activeUser);

        rvChat = findViewById(R.id.rvChat);
        chatAdapter = new ChatAdapter(MessageActivity.this, activeUser, messages);
        rvChat.setAdapter(chatAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);

        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender",ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient",activeUser);

        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("recipient",ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender",activeUser);

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);

        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    if (objects.size()>0){
                        messages.clear();
                        for(ParseObject parseObject: objects){
                            Message message = new Message();
                            String messageContent = parseObject.getString("message");
                            if (!parseObject.getString("sender").equals(ParseUser.getCurrentUser().getUsername())){
                                message.setUserId("bekanur");
                            }
                            message.setBody(messageContent);

                            messages.add(message);

                        }

                        chatAdapter.notifyDataSetChanged();

                    }
                }
            }
        });




    }


}