package com.boom.battleships.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boom.battleships.R;
import com.boom.battleships.views.RowItem;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Ximena on 29/3/2018.
 */

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
        TextView txtTurn;
        TextView txtMatch;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);
        Log.d("position", String.valueOf(position));

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_game, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtTurn=(TextView) convertView.findViewById(R.id.txtTurn);
            holder.txtMatch=(TextView) convertView.findViewById(R.id.idmatch);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.txtName.setText(rowItem.getName());
        Picasso.get().load(rowItem.getImageId()).into(holder.imageView);
        String matchS= String.valueOf(rowItem.getMatch());
        holder.txtMatch.setText(matchS);

        holder.txtTurn.setText(rowItem.getTurn());
        return convertView;
    }

}
