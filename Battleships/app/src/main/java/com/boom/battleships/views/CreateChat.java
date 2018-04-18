package com.boom.battleships.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.ChatsAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.model.Chat;
import com.boom.battleships.model.Friend;
import com.boom.battleships.model.User;
import com.boom.battleships.utils.BoomUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateChat extends AppCompatActivity implements ApiCaller {
    private View overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        overlay = findViewById(R.id.progress_overlay);
        APICalls.get("user/friends", this);
        BoomUtils.animateView(overlay, View.VISIBLE, 0.4f, 200);
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        JSONArray friends = response.optJSONArray("friendships");
        ArrayList<Friend> lFriends = new ArrayList<>();
        JSONObject jsonFriendship, jsonFriend;
        Friend friend;
        String name, picture;
        int friendshipId, id;

        for(int i = 0; i < friends.length(); i++) {
            jsonFriendship = friends.optJSONObject(i);
            jsonFriend = jsonFriendship.optJSONObject("friend");


            friendshipId = jsonFriendship.optInt("id");
            id = jsonFriend.optInt("id");
            name = jsonFriend.optString("name");
            picture = jsonFriend.optString("picture");

            friend = new Friend(friendshipId, id, name, picture);
            lFriends.add(friend);
        }

        User.getInstance().setFriends(lFriends);
        ChatsAdapter adapter = new ChatsAdapter(this, lFriends);

        ListView friendsList = findViewById(R.id.listFriends);
        friendsList.setAdapter(adapter);

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
