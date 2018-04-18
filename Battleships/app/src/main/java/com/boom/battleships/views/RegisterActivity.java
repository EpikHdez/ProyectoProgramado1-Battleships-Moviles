package com.boom.battleships.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.asynctasks.UploadImage;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;
import com.boom.battleships.utils.BoomUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private Uri selectedImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    public static final String Picture = "pictureKey";
    public static final String Email = "emailKey";
    private String passTemp;

    private View overlay;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        overlay = findViewById(R.id.progress_overlay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode != REQUEST_IMAGE_CAPTURE ) {
            if (requestCode == 1001) {
                Uri imageUri = data.getData();
                ImageView picture = findViewById(R.id.imgPicture);
                Picasso.get().load(imageUri).into(picture);
                selectedImageUri = imageUri;
            }
        }else{
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
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
        BoomUtils.animateView(overlay, View.VISIBLE, 0.4f, 200);

        if(selectedImageUri != null)
            new UploadImage(this).execute(selectedImageUri);
        else
            registerUser(null);
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

    private void openHomeActivity() {
        BoomUtils.animateView(overlay, View.GONE,0, 200);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void receiveAsyncResponse(Object response) {
        //TODO handle the response from the upload to cloudinary
        registerUser(response.toString());
    }

    private void registerUser(String pictureURL) {
        EditText txtName = findViewById(R.id.txtName);
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPassword = findViewById(R.id.txtPassword);

        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        passTemp = txtPassword.getText().toString();

        if(name.equals("") || email.equals("") || passTemp.equals("")) {
            Toast.makeText(this, R.string.cantBeBlank, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject data = new JSONObject();

        try {
            data.put("name", txtName.getText().toString());
            data.put("email", txtEmail.getText().toString());
            data.put("password", txtPassword.getText().toString());
            passTemp = txtPassword.getText().toString();

            if(pictureURL != null)
                data.put("picture", pictureURL);

            APICalls.post("auth/signup", data, this);
        } catch(Exception ex) {

        }
    }

    @Override
    public void receiveApiResponse(JSONObject response) {
        try {
            JSONObject jsonUser= (JSONObject) response.get("user");
            User user=User.getInstance();
            user.setId((Integer) jsonUser.get("id"));
            user.setName((String) jsonUser.get("name"));
            user.setPicture((String) jsonUser.get("picture"));
            user.setMoney((Integer)jsonUser.get("money"));
            user.setEmail((String) jsonUser.get("email"));
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Name, (String) jsonUser.get("name"));
            editor.putString(Pass, passTemp);
            editor.putString(Email, (String) jsonUser.get("email"));
            editor.putString(Picture, (String) jsonUser.get("picture"));
            editor.commit();

            openHomeActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveApiError(VolleyError error) {
        BoomUtils.animateView(overlay, View.GONE, 0, 200);

        try {
            String message = new String(error.networkResponse.data, "UTF-8");
            JSONObject errorJson = new JSONObject(message);
            Iterator<String> keys = errorJson.keys();

            for (Iterator<String> it = keys; it.hasNext(); ) {
                String key = it.next();

                if(key.equals("email")) {
                    Toast.makeText(this, R.string.emailDuplicated, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
