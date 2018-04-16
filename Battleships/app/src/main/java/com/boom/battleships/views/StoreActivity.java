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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private ApiCaller caller;
    public void setElements(JSONArray jsonArray){
        ListView elements;

        List<Element> elementsList = new ArrayList<Element>();

        for (int i = 0; i < jsonArray.length(); i++) {

            Element item;
            try {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                int id= (int) jsonObject.get("id");
                String name= (String) jsonObject.get("name");
                int price= (int) jsonObject.get("price");
                String picture= (String) jsonObject.get("picture");
                item = new Element(id,picture,name,price);
                elementsList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        elements = (ListView) findViewById(R.id.items);
        CustomListStoreAdapter adapter = new CustomListStoreAdapter(this,
                R.layout.listview_game, elementsList);
        elements.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        caller=this;
        setContentView(R.layout.activity_store);
        APICalls.get("item",caller);



    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("ResponseStore",response.toString());
        JSONArray items= null;
        try {
            items = response.getJSONArray("items");
            setElements(items);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}
