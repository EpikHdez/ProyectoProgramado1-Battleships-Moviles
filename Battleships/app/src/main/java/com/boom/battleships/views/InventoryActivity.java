package com.boom.battleships.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.CustomListInventoryAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.ElementInventory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private ApiCaller caller;
    public void setElements(JSONArray jsonArray){
        ListView elements;

        List<ElementInventory> elementsList = new ArrayList<ElementInventory>();

        for (int i = 0; i < jsonArray.length(); i++) {

            ElementInventory item;
            try {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                int id= (int) jsonObject.get("id");

                int quantity= (int) jsonObject.get("quantity");
                JSONObject itemN=jsonObject.getJSONObject("item");
                Log.d("itemN",itemN.toString());
                String name= (String) itemN.get("name");
                String picture= (String) itemN.get("picture");
                int idItem= (int) itemN.get("id");
                item = new ElementInventory(idItem, quantity, idItem, name, picture) ;
                elementsList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        elements = (ListView) findViewById(R.id.itemsList);
        CustomListInventoryAdapter adapter = new CustomListInventoryAdapter(this,
                R.layout.listview_inventory, elementsList);
        elements.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        caller=this;
        APICalls.get("user/items",caller);
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("Response",response.toString());
        try {
            JSONArray inventory=response.getJSONArray("inventory");
            setElements(inventory);
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
