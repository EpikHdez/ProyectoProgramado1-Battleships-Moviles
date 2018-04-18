package com.boom.battleships.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.ChatsAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.model.Friend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindFriends extends AppCompatActivity implements ApiCaller {
    private int flag, index;
    private List<Friend> users;

    private ChatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        flag = 0;
        APICalls.get("users", this);
    }

    private void fillUsers(JSONArray jUsers) {
        users = new ArrayList<>();
        JSONObject jUser;
        Friend user;
        String name, picture;
        int id;

        for(int i = 0; i < jUsers.length(); i++) {
            jUser = jUsers.optJSONObject(i);

            id = jUser.optInt("id");
            name = jUser.optString("name");
            picture = jUser.optString("picture");

            user = new Friend(id, name, picture);
            users.add(user);
        }

        adapter = new ChatsAdapter(this, users);
        ListView usersListView = findViewById(R.id.usersList);
        usersListView.setAdapter(adapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                addFriend(index);
            }
        });
    }

    private void addFriend(int index) {
        Friend user = users.get(index);
        JSONObject data = new JSONObject();

        try {
            data.put("friend_id", user.getId());

            flag = 1;
            APICalls.post("user/friend", data, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        switch (flag) {
            case 0:
                fillUsers(response.optJSONArray("users"));
                break;

            case 1:
                Toast.makeText(this, R.string.friendAdded, Toast.LENGTH_LONG).show();
                Friend friend = users.remove(index);
                adapter.remove(friend);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void receiveApiError(VolleyError error) {
        Toast.makeText(this, R.string.noFriendAdded, Toast.LENGTH_LONG).show();
    }
}
