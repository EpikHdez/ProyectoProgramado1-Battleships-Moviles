package com.boom.battleships.views;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private Uri selectedImageUri;
    private ApiCaller caller;
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

        if(resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                Uri imageUri = data.getData();
                ImageView picture = findViewById(R.id.imgProfile);
                Picasso.get().load(imageUri).into(picture);
                selectedImageUri = imageUri;
            }
        }
    }
    public void changeProfile(View view){
        JSONObject jsonObject=new JSONObject();
        User user= User.getInstance();
        TextView nametxt=findViewById(R.id.txtName);
        TextView mailtxt=findViewById(R.id.txtMail);
        String nameS= (String) nametxt.getText();
        String mailS= (String) mailtxt.getText();
        try {
            jsonObject.put("picture",user.getPicture());
            jsonObject.put("name",nameS);
            jsonObject.put("email",mailS);
            APICalls.put("me",jsonObject,caller);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user= User.getInstance();
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

    @Override
    public void receiveApiResponse(JSONObject response) {

    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}
