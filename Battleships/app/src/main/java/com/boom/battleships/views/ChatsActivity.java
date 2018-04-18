package com.boom.battleships.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.ChatsAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.model.Friend;
import com.boom.battleships.model.User;
import com.boom.battleships.utils.BoomUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity implements ApiCaller {
    private View overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        overlay = findViewById(R.id.progress_overlay);

        FloatingActionButton fab = findViewById(R.id.createChatFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateChat.class);
                startActivity(intent);
            }
        });

        BoomUtils.animateView(overlay, View.VISIBLE, 0.4f, 200);
        APICalls.get("user/chats", this);
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        JSONArray chats = response.optJSONArray("chats");
        ArrayList<Friend> lChats = new ArrayList<>();
        JSONObject chat, jfriend;
        int friendshipId, userId;
        String name, picture;
        Friend friend;

        for(int i = 0; i < chats.length(); i++) {
            chat = chats.optJSONObject(i);


            friendshipId = chat.optInt("id");
            jfriend = chat.optJSONObject("friend");
            userId = jfriend.optInt("id");
            name = jfriend.optString("name");
            picture = jfriend.optString("picture");

            friend = new Friend(friendshipId, userId, name, picture);
            lChats.add(friend);
        }

        User.getInstance().setFriends(lChats);

        ChatsAdapter adapter = new ChatsAdapter(this, lChats);
        ListView chatsList = findViewById(R.id.listChats);

        chatsList.setAdapter(adapter);

        chatsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("friendIndex", i);
                startActivity(intent);
            }
        });

        BoomUtils.animateView(overlay, View.GONE, 0, 200);
    }

    @Override
    public void receiveApiError(VolleyError error) {

    }
}
