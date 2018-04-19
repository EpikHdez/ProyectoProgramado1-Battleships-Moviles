package com.boom.battleships.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.ChatsAdapter;
import com.boom.battleships.adapters.CustomListViewAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.Friend;
import com.boom.battleships.model.User;
import com.boom.battleships.utils.BoomUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private View overlay;
    private List<Friend> friends;

    private ApiCaller caller;
    private int flag;
    public void randomFriendMatch(View view){
        flag=0;
        APICalls.get("random/user",caller);
    }
    private void openBoardActivity() {
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        overlay = findViewById(R.id.progress_overlay);
        BoomUtils.animateView(overlay, View.VISIBLE, 0.4f, 200);

        caller=this;
        flag=2;
        APICalls.get("user/friends", this);
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        switch (flag){
            case 0:
                Log.d("UserRival",response.toString());
                try {
                    JSONObject json=response.getJSONObject("user");
                    int rival_id= json.getInt("id");
                    JSONObject rival=new JSONObject();
                    rival.put("rival_id",rival_id);
                    APICalls.post("user/match",rival,caller);
                    flag=1;



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                Log.d("Match",response.toString());
                User user= User.getInstance();
                try {
                    user.setCurrentGame(response.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                openBoardActivity();
                break;

            case 2:
                JSONArray jFriends = response.optJSONArray("friendships");
                JSONObject jFriendship, jFriend;
                Friend friend;
                String name, picture;
                int friendshipID, id;
                friends = new ArrayList<>();

                for(int i = 0; i < jFriends.length(); i++) {
                    jFriendship = jFriends.optJSONObject(i);
                    jFriend = jFriendship.optJSONObject("friend");

                    friendshipID = jFriendship.optInt("id");
                    name = jFriend.optString("name");
                    picture = jFriend.optString("picture");
                    id = jFriend.optInt("id");

                    friend = new Friend(friendshipID, id, name, picture);
                    friends.add(friend);
                }

                ChatsAdapter adapter = new ChatsAdapter(this, friends);
                ListView friendsListView = findViewById(R.id.friends);

                friendsListView.setAdapter(adapter);

                friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        flag = 1;
                        try {
                            JSONObject rival = new JSONObject();
                            rival.put("rival_id", friends.get(i).getId());
                            APICalls.post("user/match", rival, caller);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        }


    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}

