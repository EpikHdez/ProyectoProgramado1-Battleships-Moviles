package com.boom.battleships.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.Element;
import com.boom.battleships.model.User_Top;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private ApiCaller caller;

    public void setScores(JSONArray jsonArray){
        ListView scores;

        List<User_Top> userList = new ArrayList<User_Top>();

        for (int i = 0; i < jsonArray.length(); i++) {

            User_Top user_top;
            try {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                int id= (int) jsonObject.get("id");
                String name= (String) jsonObject.get("name");
                int score= (int) jsonObject.get("score");
                String picture= (String) jsonObject.get("picture");
                user_top = new User_Top(id,name,picture,score);
                userList.add(user_top);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        scores = (ListView) findViewById(R.id.listScores);
        CustomListScoreAdapter adapter = new CustomListScoreAdapter(this,
                R.layout.listview_score, userList);
        scores.setAdapter(adapter);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        caller=this;
        APICalls.get("user/top",caller);
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("Scores",response.toString());
        JSONArray jsonArray= null;
        try {
            jsonArray = response.getJSONArray("users");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setScores(jsonArray);

    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}
