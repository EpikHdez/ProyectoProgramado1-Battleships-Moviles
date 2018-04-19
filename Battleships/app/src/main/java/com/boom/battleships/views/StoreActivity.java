package com.boom.battleships.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.adapters.CustomListStoreAdapter;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.Element;
import com.boom.battleships.model.User;
import com.boom.battleships.utils.BoomUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private ApiCaller caller;
    User user;
    private View overlay;
    private int flag;

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

        BoomUtils.animateView(overlay, View.GONE, 0, 200);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        overlay = findViewById(R.id.progress_overlay);

        BoomUtils.animateView(overlay, View.VISIBLE, 0.4f, 200);

        caller=this;
        user= User.getInstance();

        TextView moneyTXT=findViewById(R.id.txtMoney);
        String money="Dinero: "+String.valueOf(user.getMoney());
        Log.d("Money1",money);
        moneyTXT.setText(money);

        flag = 0;
        APICalls.get("item",caller);



    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        switch (flag) {
            case  0:
                Log.d("ResponseStore",response.toString());
                JSONArray items= null;
                try {
                    items = response.getJSONArray("items");
                    setElements(items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                Toast.makeText(this, R.string.itemBought, Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void receiveApiError(VolleyError error) {
        switch (flag) {
            case 0:
                break;

            case 1:
                Toast.makeText(this, R.string.noItemBought, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}
