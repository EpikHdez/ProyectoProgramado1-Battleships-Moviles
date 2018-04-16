package com.boom.battleships.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.asynctasks.UploadImage;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private Uri selectedImageUri;
    private ApiCaller caller;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private User user;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public void onImgPictureClicked(View view) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Set your required file type
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        String title = getResources().getString(R.string.selectImage);
        startActivityForResult(Intent.createChooser(intent, title),1001);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode != REQUEST_IMAGE_CAPTURE) {
            if (requestCode == 1001) {
                Uri imageUri = data.getData();
                ImageView picture = findViewById(R.id.imgProfile);
                Picasso.get().load(imageUri).into(picture);
                selectedImageUri = imageUri;

            }
        }else{
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                ImageView picture = findViewById(R.id.imgProfile);
                Picasso.get().load(imageUri).into(picture);
                selectedImageUri = imageUri;

            }

        }

    }
    public void changeProfile(View view){
        if(selectedImageUri != null) {
            Log.d("Subiendo", "si");
            new UploadImage(this).execute(selectedImageUri);
        }else {
            Log.d("Subiendo", "no");
            registerUser(null);
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user= User.getInstance();
        caller=this;
        Log.d("Name",user.getName());
        Log.d("Email",user.getEmail());
        Log.d("Picture",user.getPicture());

        ImageView picture = findViewById(R.id.imgProfile);
        Picasso.get().load(user.getPicture()).into(picture);
        TextView txtname= findViewById(R.id.txtName);
        TextView txtmail= findViewById(R.id.txtMail);
        txtname.setText(user.getName());
        txtmail.setText(user.getEmail());

    }
    private void registerUser(String pictureURL) {
        //TODO register user into the DB
        JSONObject jsonObject=new JSONObject();
        User user= User.getInstance();
        ImageView imageView=findViewById(R.id.imgProfile);
        TextView nametxt=findViewById(R.id.txtName);
        TextView mailtxt=findViewById(R.id.txtMail);
        String nameS= nametxt.getText().toString();
        String mailS= mailtxt.getText().toString();
        Picasso.get().load(pictureURL).into(imageView);
        try {
            jsonObject.put("picture",pictureURL);
            jsonObject.put("name",nameS);
            jsonObject.put("email",mailS);
            APICalls.put("me",jsonObject,caller);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("ResponseProfile",response.toString());
        try {
            String name=response.getString("name");
            String email=response.getString("email");
            String picture=response.getString("picture");
            user.setEmail(email);
            user.setName(name);
            user.setPicture(picture);
            ImageView imageView=findViewById(R.id.imgProfile);
            TextView nametxt=findViewById(R.id.txtName);
            TextView mailtxt=findViewById(R.id.txtMail);
            nametxt.setText(user.getName());
            mailtxt.setText(user.getEmail());
            Picasso.get().load(user.getPicture()).into(imageView);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {
        registerUser(response.toString());

    }
}
