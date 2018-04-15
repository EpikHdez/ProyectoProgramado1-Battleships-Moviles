package com.boom.battleships.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.ChatAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.model.Friend;
import com.boom.battleships.model.Message;
import com.boom.battleships.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ApiCaller {

    private Friend friend;
    private int flag = 0;

    private EditText messageET;
    private ListView messagesContainer;
    private ImageButton sendBtn;
    private ChatAdapter adapter;
    private ArrayList<Message> chatHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int friendIndex = getIntent().getIntExtra("friendIndex", -1);

        if(friendIndex < 0) {
            finish();
            return;
        }

        initControls();
        chatHistory = new ArrayList<>();

        List<Friend> friends = User.getInstance().getFriends();
        friend = friends.get(friendIndex);
        String URL = String.format("user/messages/%d", friend.getFriendshipId());

        APICalls.get(URL, this);
    }

    private void initControls() {
        messagesContainer = findViewById(R.id.messagesContainer);

        if(adapter == null){
            adapter = new ChatAdapter(ChatActivity.this, new ArrayList<Message>());
        }
        messagesContainer.setAdapter(adapter);

        messageET = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.chatSendButton);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();

                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                Message chatMessage = new Message(User.getInstance().getId(), messageText);
                sendMessage(chatMessage);
                displayMessage(chatMessage);

                messageET.setText("");
            }
        });
    }

    private void sendMessage(Message message) {
        JSONObject json = new JSONObject();

        try {
            json.put("friendship_id", friend.getFriendshipId());
            json.put("text", message.getText());

            APICalls.post("user/message", json, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void displayMessage(Message message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        if(flag == 0) {
            JSONArray messages = response.optJSONObject("conversation").optJSONArray("messages");
            JSONObject jMessage;
            Message message;
            int user;
            String text, date;

            for(int i = 0; i < messages.length(); i++) {
                jMessage = messages.optJSONObject(i);

                try {
                    user = jMessage.optInt("user");
                    text = jMessage.optString("text");
                    date = jMessage.optString("date").split("T")[0];

                    message = new Message(user, text, date);
                    chatHistory.add(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            adapter.add(chatHistory);
            adapter.notifyDataSetChanged();
            scroll();
            flag = 1;
        }
    }

    @Override
    public void receiveApiError(VolleyError error) {

    }
}
