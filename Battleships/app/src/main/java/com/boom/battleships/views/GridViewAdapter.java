package com.boom.battleships.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boom.battleships.R;

import java.util.ArrayList;

/**
 * Created by Ximena on 29/3/2018.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private final ArrayList<ImageView> imageViews;

    public GridViewAdapter(Context context, ArrayList<ImageView> imageViews) {
        this.context = context;
        this.imageViews = imageViews;

    }
    public void changeImage(int position, int image){
        imageViews.get(position).setImageResource(image);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView image= imageViews.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        if (convertView == null) {

            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.gridview_game, null);







        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        imageView.setImageResource(R.drawable.wave);

        imageViews.set(position,imageView);

        Log.d("position", String.valueOf(position));


        return convertView;

    }


    @Override

    public int getCount() {

        return imageViews.size();

    }


    @Override

    public Object getItem(int position) {
        Log.d("Posicion desde aqui", String.valueOf(position));
        return imageViews.get(position);

    }


    @Override

    public long getItemId(int position) {

        return imageViews.get(position).getId();

    }

}

