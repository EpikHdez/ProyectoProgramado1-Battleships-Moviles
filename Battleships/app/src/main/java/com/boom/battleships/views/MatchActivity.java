package com.boom.battleships.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.CustomListViewAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {


    private ApiCaller caller;
    private int flag;
    public void randomFriendMatch(View view){
        openBoardActivity();
    }
    private void openBoardActivity() {
        flag=0;
        APICalls.get("random/user",caller);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        caller=this;
        ListView friends = findViewById(R.id.friends);
        List<RowItem> rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < 3; i++) {
            RowItem item = new RowItem("https://i1.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png?resize=256%2C256&quality=100","Name", "",-1);

            rowItems.add(item);
        }
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.listview_game, rowItems);
        friends.setAdapter(adapter);
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
                Intent intent = new Intent(this, BoardActivity.class);
                startActivity(intent);

                break;

        }


    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}

