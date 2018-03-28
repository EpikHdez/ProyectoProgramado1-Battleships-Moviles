package com.boom.battleships.views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.asynctasks.UploadImage;
import com.boom.battleships.interfaces.ApiCallRequester;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCallRequester {
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                Uri imageUri = data.getData();
                ImageView picture = findViewById(R.id.imgPicture);
                Picasso.get().load(imageUri).into(picture);
                selectedImageUri = imageUri;
            }
        }
    }

    /**
     * Starts a new intent that will prompt the user to select an image.
     *
     * @param view
     */
    public void onImgPictureClicked(View view) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Set your required file type
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        String title = getResources().getString(R.string.selectImage);
        startActivityForResult(Intent.createChooser(intent, title),1001);
    }

    /**
     * Finishes this activity and returns to the login one.
     *
     * @param view
     */
    public void onBtnCancelClicked(View view) {
        finish();
    }

    /**
     * Registers a new user into the DB and redirects him to the home activity.
     *
     * @param view
     */
    public void onBtnRegisterClicked(View view) {
        new UploadImage(this).execute(selectedImageUri);
        //TODO register user into the DB
    }

    /**
     * Initializes the process to recover an already registered user's password.
     *
     * @param view
     */
    public void onLblRecoverPasswordClicked(View view) {
        //TODO handle the recover password process
    }

    @Override
    public void receiveAsyncResponse(Object response) {
        //TODO handle the response from the upload to cloudinary
        EditText txtName = findViewById(R.id.txtName);
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPassword = findViewById(R.id.txtPassword);

        JSONObject data = new JSONObject();

        try {
            data.put("name", txtName.getText().toString());
            data.put("email", txtEmail.getText().toString());
            data.put("password", txtPassword.getText().toString());
            data.put("profile_picture", response.toString());

            APICalls.post("user", data, this);
        } catch(Exception ex) {

        }
    }

    @Override
    public void receiveApiResponse(Object response) {
        System.out.println(response);
    }

    @Override
    public void receiveApiError(Object error) {

    }
}
