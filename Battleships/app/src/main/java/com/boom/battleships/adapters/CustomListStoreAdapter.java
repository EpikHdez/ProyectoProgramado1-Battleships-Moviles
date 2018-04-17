package com.boom.battleships.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.Element;
import com.boom.battleships.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ximena on 14/4/2018.
 */

public class CustomListStoreAdapter extends ArrayAdapter<Element> implements AsyncTaskRequester, ApiCaller {
    Context context;
    private ApiCaller caller;
    public CustomListStoreAdapter(Context context, int resourceId,
                                 List<Element> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("RESPONSEBUY",response.toString());


    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }

    /*private view holder class*/
    private class ViewHolderElement {
        ImageView imageView;
        TextView txtName;
        TextView txtPrice;
        Button button;


    }


    public View getView(int position, View convertView, ViewGroup parent) {
        caller=this;
        CustomListStoreAdapter.ViewHolderElement holder = null;
        final Element element = getItem(position);
        Log.d("position", String.valueOf(position));

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_store, null);
            holder = new CustomListStoreAdapter.ViewHolderElement();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtPrice=(TextView) convertView.findViewById(R.id.txtPrice);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.button= (Button) convertView.findViewById(R.id.btnShop);

            convertView.setTag(holder);
        } else
            holder = (CustomListStoreAdapter.ViewHolderElement) convertView.getTag();


        holder.txtName.setText(element.getName());
        String price=String.valueOf(element.getPrice());
        Log.d("Price",price);
        holder.txtPrice.setText(price);
        Picasso.get().load(element.getImage()).into(holder.imageView);
        holder.button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                JSONObject jsonObject=new JSONObject();
                try {

                    User user = User.getInstance();
                    if(user.getMoney()>=element.getPrice()) {
                        jsonObject.put("quantity",1);
                        APICalls.post("item/"+String.valueOf(element.getId())+"/buy",jsonObject,caller);
                        user.setMoney(user.getMoney() - element.getPrice());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });


        return convertView;
    }

}