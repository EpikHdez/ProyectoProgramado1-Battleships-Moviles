package com.boom.battleships.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ConfigurationActivity extends AppCompatActivity {
    private User user;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onResume() {


        super.onResume();
        ImageView picture = findViewById(R.id.imgProfile);
        Picasso.get().load(user.getPicture()).into(picture);
        TextView txtname = findViewById(R.id.txtName);
        txtname.setText(user.getName());
        Button btnSound = findViewById(R.id.btnSound);
        if (user.isMusic()) {
            btnSound.setText("Si");
        } else {
            btnSound.setText("No");
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        user= User.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ImageView picture = findViewById(R.id.imgProfile);
        Picasso.get().load(user.getPicture()).into(picture);
        TextView txtname= findViewById(R.id.txtName);
        txtname.setText(user.getName());
        Button btnSound= findViewById(R.id.btnSound);
        if(user.isMusic()){
            btnSound.setText("Si");
        }else{
            btnSound.setText("No");
        }

    }
    public void openEditProfileActivity(View view) {
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);

    }
    public void soundConfiguration(View view) {
        Button btnSound= findViewById(R.id.btnSound);
        if(user.isMusic()){
            user.setMusic(false);
            user.getMusicBack().stop();

            btnSound.setText("Si");
        }else{
            MediaPlayer ring= MediaPlayer.create(this,R.raw.music);
            ring.setLooping(true);
            ring.start();
            user.setMusic(true);
            user.setMusicBack(ring);
            btnSound.setText("No");
        }


    }
    public void logOut(View view) {
       finish();
       Intent intent = new Intent(this,LoginActivity.class);
       startActivity(intent);
       user.setMusic(false);
       user.getMusicBack().stop();
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.commit();



    }
}
