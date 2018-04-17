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
import com.boom.battleships.model.ElementInventory;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ximena on 14/4/2018.
 */

public class CustomListInventoryAdapter extends ArrayAdapter<ElementInventory> implements AsyncTaskRequester, ApiCaller {
    Context context;
    private ApiCaller caller;
    public CustomListInventoryAdapter(Context context, int resourceId,
                                      List<ElementInventory> items) {
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
    private class ViewHolderElementInventory{
        ImageView imageView;
        TextView txtName;
        TextView txtQuantity;
        Button button;


    }


    public View getView(int position, View convertView, ViewGroup parent) {
        caller=this;
        CustomListInventoryAdapter.ViewHolderElementInventory holder = null;
        final ElementInventory element = getItem(position);
        Log.d("position", String.valueOf(position));

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_inventory, null);
            holder = new CustomListInventoryAdapter.ViewHolderElementInventory();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.button= (Button) convertView.findViewById(R.id.btnUse);

            convertView.setTag(holder);
        } else
            holder = (CustomListInventoryAdapter.ViewHolderElementInventory) convertView.getTag();


        holder.txtName.setText(element.getName());

        String quantity=String.valueOf(element.getQuantity());

        holder.txtQuantity.setText(String.valueOf(quantity));
        Picasso.get().load(element.getPicture()).into(holder.imageView);
        holder.button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                JSONObject jsonObject=new JSONObject();
                APICalls.put("user/item/"+String.valueOf(element.getIdItem()),jsonObject,caller);




            }
        });


        return convertView;
    }

}