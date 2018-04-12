package com.boom.battleships.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity  implements AsyncTaskRequester, ApiCaller {
    //Facebook needed variables
    private CallbackManager cbManager;
    private LoginButton loginButton;
    private JSONObject facebookParameters;
    private String pass;
    public void getFriends(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{friend-list-id}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                            Log.d("friends",response.toString());
            /* handle the result */
                    }
                }
        ).executeAsync();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setUpFacebookLoginButton();

        if(isLoggedIn()) {
            openHomeActivity();
        }
    }

    /**
     * This is called when the login attempt from Facebook ends, this is because this process occurs
     * on a different activity and its result is communicated to this one through this method.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cbManager.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Set up all Facebook login button related configuration to allow the login into the
     * application with its API.
     */
    private void setUpFacebookLoginButton() {
        cbManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);

        final List<String> readPermissions = Arrays.asList("public_profile", "email","user_friends");
        loginButton.setReadPermissions(readPermissions);

        // Callback registration
        loginButton.registerCallback(cbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() != null) {
                    getFriends();
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.v("LoginActivity", response.toString());

                                    // Application code


                                        Log.d("FacebookResponse:",object.toString());
                                    JSONObject data = new JSONObject();

                                    try {

                                        User user= User.getInstance();
                                        Log.d("Name", (String) object.get("name"));
                                        data.put("name", object.get("name"));
                                        data.put("email", object.get("email"));
                                        JSONObject picture= (JSONObject) object.get("picture");
                                        JSONObject pictureData= (JSONObject) picture.get("data");

                                        data.put("profile_picture",  pictureData.get("url"));
                                        Log.d("Picture", (String) data.get("profile_picture"));
                                        user.setName((String) object.get("name"));
                                        user.setEmail((String) object.get("email"));
                                        user.setPicture((String) pictureData.get("url"));


                                        requestPassword();
                                        Log.d("Password",pass);
                                        data.put("password",pass);
                                        facebookParameters=data;

                                        Log.d("FacebookParameters",facebookParameters.toString());



                                    } catch(Exception ex) {
                                        Log.d("Exception", ex.toString());

                                    }


                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday, picture,friends");

                    request.setParameters(parameters);
                    request.executeAsync();
                    //APICalls.post("auth/signup", data, this);
                    User user= User.getInstance();
                    Log.d("Usuario",user.toString());
                    //


                    //


                }
            }

            @Override
            public void onCancel() {
                showToastMessage(R.string.onFacebookLoginCancelled, Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("FacebookException", String.format("onError: %s", exception.getMessage()));
                showToastMessage(R.string.onFacebookLoginError, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * Checks if there's an existing user already logged in the application.
     *
     * @return true if token exists, false otherwise.
     */
    private boolean isLoggedIn() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null;
    }

    /**
     * Opens the home activity and calls to finish this Activity, this is for no letting it in the
     * Activity stack, so when the user press the back button, the application doesn't come back to
     * this one.
     */
    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        //Uncomment to finish this activity and avoid going back here when the back button is pressed.
        //finish();
    }
    private void requestPassword(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese una Contraseña");
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            String m_Text = "";
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                pass=m_Text;
                Log.d("m_Text",pass);

                //openHomeActivity();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    /**
     * Shows a Toast notification within this context.
     *
     * @param stringId the string id to show.
     * @param length the time the notification will be visible.
     */
    private void showToastMessage(int stringId, int length) {
        Toast.makeText(this, stringId, length).show();
    }

    /**
     * Method to begin the registration process with an email and password different from facebook,
     * this should only be called when the btnRegisterWithEmail is clicked.
     *
     * @param view the view that called the method.
     */
    public void onBtnRegisterWithEmailClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
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
