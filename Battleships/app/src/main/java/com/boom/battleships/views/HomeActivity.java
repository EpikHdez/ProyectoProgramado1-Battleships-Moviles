package com.boom.battleships.views;

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
import com.boom.battleships.adapters.CustomListViewAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;
import com.boom.battleships.utils.BoomUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    User user;
    private ApiCaller caller;
    private int flag;
    private View overlay;

    public void openStoreActivity(View view) {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
    }
    public void openHelpActivity(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
    public void openChatsActivity(View view) {
        Intent intent = new Intent(this, ChatsActivity.class);
        startActivity(intent);
    }
    public void openScoreActivity(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }
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
        overlay = findViewById(R.id.progress_overlay);
        BoomUtils.animateView(overlay, View.VISIBLE, 0.4f, 200);

        caller=this;

        user=User.getInstance();
        Log.d("User",user.toString());
        APICalls.get("user/matches",caller);



    }
    @Override
    protected void onResume() {


        super.onResume();
        APICalls.get("user/matches", caller);


    }
    public void showMatches(JSONObject response){
        Log.d("showMatches",response.toString());
        ListView currentGamesView;
        ListView finishedGamesView;
        try {
            JSONArray matches= response.getJSONArray("matches");
            List<RowItem> rowItems = new ArrayList<RowItem>();
            List<RowItem> rowItemsfinished = new ArrayList<RowItem>();
            for (int i = 0; i < matches.length(); i++) {
                JSONObject jsonObject=matches.getJSONObject(i);
                boolean turn = (Boolean) jsonObject.get("turn");
                boolean finished = (Boolean) jsonObject.get("finished");
                int id = (Integer) jsonObject.get("id");
                JSONObject rival = (JSONObject) jsonObject.get("rival");
                String name = (String) rival.get("name");
                String picture = (String) rival.get("picture");
                RowItem item;
                if(!finished) {


                    if (turn) {
                        item = new RowItem(picture, name, "Su turno.", id);
                    } else {
                        item = new RowItem(picture, name, "Espere su turno.", id);

                    }

                    rowItems.add(item);
                }else{
                    boolean victory = (Boolean) jsonObject.get("victory");
                    if (victory) {
                        item = new RowItem(picture, name, "Ganador.", id);
                    } else {
                        item = new RowItem(picture, name, "Perdedor.", id);

                    }
                    rowItemsfinished.add(item);

                }
            }
            currentGamesView = (ListView) findViewById(R.id.currentGames);
            CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                    R.layout.listview_game, rowItems);
            currentGamesView.setAdapter(adapter);
            currentGamesView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtturn = view.findViewById(R.id.txtTurn);
                        if(txtturn.getText().equals("Su turno.")) {
                            TextView txtmatch = view.findViewById(R.id.idmatch);
                            Log.d("NumMatch", String.valueOf(txtmatch.getText()));

                            user.setCurrentGame(Integer.parseInt((String) txtmatch.getText()));
                            openBoardActivity();
                        }

                }


            });
            CustomListViewAdapter adapterFinished = new CustomListViewAdapter(this,
                    R.layout.listview_game, rowItemsfinished);

            finishedGamesView = (ListView) findViewById(R.id.finishedGames);
            finishedGamesView .setAdapter(adapterFinished);

            BoomUtils.animateView(overlay, View.GONE, 0, 200);
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
            case 1:
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
