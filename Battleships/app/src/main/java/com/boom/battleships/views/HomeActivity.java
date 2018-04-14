package com.boom.battleships.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    User user;
    private ApiCaller caller;
    private int flag;
    private void openBoardActivity() {
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);


    }
    private void openMatchActivity() {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);


    }

    public void startGame(View view){
        openMatchActivity();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        caller=this;

        user=User.getInstance();
        Log.d("User",user.toString());
        APICalls.get("user/matches",caller);



    }
    public void showMatches(JSONObject response){
        ListView currentGamesView;
        ListView finishedGamesView;
        try {
            JSONArray matches= response.getJSONArray("matches");
            List<RowItem> rowItems = new ArrayList<RowItem>();
            List<RowItem> rowItemsfinished = new ArrayList<RowItem>();
            for (int i = 0; i < matches.length(); i++) {
                JSONObject jsonObject=matches.getJSONObject(i);

                boolean turn= (Boolean) jsonObject.get("turn");
                int id= (Integer) jsonObject.get("id");
                JSONObject rival= (JSONObject) jsonObject.get("rival");
                String name= (String) rival.get("name");
                String picture= (String) rival.get("picture");
                RowItem item;
                if(turn){
                    item = new RowItem(picture,name, "Su turno.",id);
                }else{
                    item = new RowItem(picture,name, "Espere su turno.",id);
                }

                rowItems.add(item);
            }
            currentGamesView = (ListView) findViewById(R.id.currentGames);
            CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                    R.layout.listview_game, rowItems);
            currentGamesView.setAdapter(adapter);
            currentGamesView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView txtmatch=view.findViewById(R.id.idmatch);
                        Log.d("NumMatch", String.valueOf(txtmatch.getText()));
                        openBoardActivity();
                        user.setCurrentGame(Integer.parseInt((String) txtmatch.getText()));

                }


            });
            finishedGamesView = (ListView) findViewById(R.id.finishedGames);
            finishedGamesView .setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("ResponseHome",response.toString());
        switch (flag){
            case 0:

                showMatches(response);
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
