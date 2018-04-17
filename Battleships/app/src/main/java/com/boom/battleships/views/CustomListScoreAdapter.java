package com.boom.battleships.views;

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
import com.boom.battleships.model.User_Top;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ximena on 14/4/2018.
 */

public class CustomListScoreAdapter extends ArrayAdapter<User_Top> implements AsyncTaskRequester, ApiCaller {
    Context context;
    private ApiCaller caller;
    public CustomListScoreAdapter(Context context, int resourceId,
                                  List<User_Top> users) {
        super(context, resourceId, users);
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
        TextView txtScore;
        ImageView troffe;
        TextView position;



    }


    public View getView(int position, View convertView, ViewGroup parent) {
        caller=this;
        CustomListScoreAdapter.ViewHolderElement holder = null;
        final User_Top user_top = getItem(position);
        Log.d("position", String.valueOf(position));

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_score, null);
            holder = new CustomListScoreAdapter.ViewHolderElement();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtScore=(TextView) convertView.findViewById(R.id.txtScore);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.troffe = (ImageView) convertView.findViewById(R.id.troffe);
            holder.position = (TextView) convertView.findViewById(R.id.txtPosition);



            convertView.setTag(holder);
        } else
            holder = (CustomListScoreAdapter.ViewHolderElement) convertView.getTag();


        holder.txtName.setText(user_top.getName());
        String score="Score: "+String.valueOf(user_top.getScore());
        String positionS="#"+String.valueOf(position+1);
        holder.position.setText(positionS);
        holder.txtScore.setText(score);
        Picasso.get().load(user_top.getPicture()).into(holder.imageView);
        switch (position){
            case 0:
                holder.troffe.setImageResource(R.drawable.gold);
                break;
            case 1:
                holder.troffe.setImageResource(R.drawable.sylver);
                break;
            case 2:
                holder.troffe.setImageResource(R.drawable.bronze);
                break;
            default:
                break;
        }



        return convertView;
    }

}