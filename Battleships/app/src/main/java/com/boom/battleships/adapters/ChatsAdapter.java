package com.boom.battleships.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boom.battleships.R;
import com.boom.battleships.model.Friend;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class ChatsAdapter extends ArrayAdapter<Friend> {
    private LayoutInflater inflater;

    public ChatsAdapter(@NonNull Context context, List<Friend> objects) {
        super(context, R.layout.listview_chats, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            v = inflater.inflate(R.layout.listview_chats, null);
        }

        Friend friend = getItem(position);

        if(friend != null) {
            ImageView picture = v.findViewById(R.id.friendPicture);
            TextView name = v.findViewById(R.id.friendName);

            Picasso.get().load(friend.getPicture()).into(picture);
            name.setText(friend.getName());
        }

        return v;
    }
}
