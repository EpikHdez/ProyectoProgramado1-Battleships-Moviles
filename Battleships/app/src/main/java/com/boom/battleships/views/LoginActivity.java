package com.boom.battleships.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCallRequester;
import com.boom.battleships.interfaces.AsyncTaskRequester;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity  implements AsyncTaskRequester, ApiCallRequester {
    //Facebook needed variables
    private CallbackManager cbManager;
    private LoginButton loginButton;

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

                                        /*String public_profile = object.getString("public_profile");
                                        String email = object.getString("email"); // 01/31/1980 format
                                        Log.d("Permisos",public_profile);
                                        Log.d("Permisos",email);*/
                                        Log.d("FacebookResponse:",object.toString());
                                    JSONObject data = new JSONObject();

                                    try {
                                        data.put("name", data.get("name"));
                                        data.put("email", data.get("email"));
                                        JSONObject picture= (JSONObject) data.get("picture");
                                        JSONObject pictureData= (JSONObject) picture.get("data");

                                        data.put("profile_picture",  pictureData.get("url"));

                                        APICalls.post("user", data, (ApiCallRequester) getApplicationContext());
                                    } catch(Exception ex) {

                                    }


                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday, picture,friends");

                    request.setParameters(parameters);
                    request.executeAsync();

                    openHomeActivity();
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
    public void receiveApiResponse(Object response) {

    }

    @Override
    public void receiveApiError(Object error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}
