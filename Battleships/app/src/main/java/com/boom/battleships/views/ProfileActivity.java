package com.boom.battleships.views;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boom.battleships.R;
import com.boom.battleships.model.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private Uri selectedImageUri;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user= User.getInstance();
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
}
