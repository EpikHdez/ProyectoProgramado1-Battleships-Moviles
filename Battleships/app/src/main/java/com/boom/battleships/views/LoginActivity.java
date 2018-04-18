package com.boom.battleships.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;
import com.boom.battleships.utils.BoomUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity  implements ApiCaller {
    //Facebook needed variables
    private CallbackManager cbManager;
    private LoginButton loginButton;
    private JSONObject facebookParameters;
    private ApiCaller caller;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    public static final String Picture = "pictureKey";
    public static final String Email = "emailKey";
    private String passTemp;
    private int flag=0;
    private View overlay;

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
    public void loginNoFace(View view){
        BoomUtils.animateView(overlay, View.VISIBLE,0.4f, 200);

        JSONObject data = new JSONObject();
        try {
            User user = User.getInstance();
            user.setEmail(sharedpreferences.getString(Email, ""));
            TextView txtemail=findViewById(R.id.txtmail);
            TextView txtpass=findViewById(R.id.txtpass);
            String email=  txtemail.getText().toString();
            String pass= txtpass.getText().toString();
            passTemp=pass;
            data.put("email", email);
            data.put("password", pass);
            Log.d("email",email);
            Log.d("pass",pass);
            flag=1;
            APICalls.post("auth/login", data, caller);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String projectToken =  "d69eec9a0db743021f142cb173cbb6d6"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);

        setContentView(R.layout.activity_login);
        caller= this;
        overlay = findViewById(R.id.progress_overlay);

        User user=User.getInstance();
        MediaPlayer ring= MediaPlayer.create(this,R.raw.music);
        ring.setLooping(true);
        ring.start();
        user.setMusicBack(ring);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setUpFacebookLoginButton();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(isLoggedIn()) {

                findViewById(R.id.btnRegisterWIthEmail).setVisibility(View.INVISIBLE);
                findViewById(R.id.btnlogin).setVisibility(View.INVISIBLE);
                findViewById(R.id.txtmail).setVisibility(View.INVISIBLE);
                findViewById(R.id.txtpass).setVisibility(View.INVISIBLE);



                Map<String,?> keys = sharedpreferences.getAll();

                for(Map.Entry<String,?> entry : keys.entrySet()){
                    Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
                }
                JSONObject data = new JSONObject();
                try {

                    user.setEmail(sharedpreferences.getString(Email, ""));

                    data.put("email",sharedpreferences.getString(Email, ""));
                    data.put("password",sharedpreferences.getString(Pass, ""));
                    flag=2;
                    APICalls.post("auth/login",data,caller);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }else{

            String username = sharedpreferences.getString(Email,null);
            String password = sharedpreferences.getString(Pass,null);
            if (username != null && password != null )
            {
                JSONObject data = new JSONObject();
                try {

                    user.setEmail(sharedpreferences.getString(Email, ""));

                    data.put("email",sharedpreferences.getString(Email, ""));
                    data.put("password",sharedpreferences.getString(Pass, ""));
                    flag=2;
                    APICalls.post("auth/login",data,caller);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


    }
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
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
        Profile fbProfile = Profile.getCurrentProfile();
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {

            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Log.d("holi", "onLogout catched");
                    findViewById(R.id.btnRegisterWIthEmail).setVisibility(View.VISIBLE);
                    findViewById(R.id.btnlogin).setVisibility(View.VISIBLE);
                    findViewById(R.id.txtmail).setVisibility(View.VISIBLE);
                    findViewById(R.id.txtpass).setVisibility(View.VISIBLE);

                }
            }
        };

            cbManager = CallbackManager.Factory.create();
            loginButton = findViewById(R.id.login_button);
            loginButton.setBackgroundResource(R.drawable.borderbuttonsface);
            final List<String> readPermissions = Arrays.asList("public_profile", "email", "user_friends");
            loginButton.setReadPermissions(readPermissions);

            // Callback registration
            loginButton.registerCallback(cbManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    if (loginResult.getAccessToken() != null) {
                        getFriends();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code

                                        Log.d("FacebookResponse:", object.toString());
                                        JSONObject data = new JSONObject();

                                        try {

                                            User user = User.getInstance();
                                            Log.d("Name", (String) object.get("name"));
                                            data.put("name", object.get("name"));
                                            data.put("email", object.get("email"));
                                            JSONObject picture = (JSONObject) object.get("picture");
                                            JSONObject pictureData = (JSONObject) picture.get("data");

                                            data.put("picture", pictureData.get("url"));
                                            Log.d("Picture", (String) data.get("picture"));
                                            user.setName((String) object.get("name"));
                                            user.setEmail((String) object.get("email"));
                                            user.setPicture((String) pictureData.get("url"));
                                            facebookParameters = data;

                                            Log.d("FacebookParameters", facebookParameters.toString());
                                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                            requestPassword(facebookParameters);

                                            findViewById(R.id.btnRegisterWIthEmail).setVisibility(View.INVISIBLE);
                                            findViewById(R.id.btnlogin).setVisibility(View.INVISIBLE);
                                            findViewById(R.id.txtmail).setVisibility(View.INVISIBLE);
                                            findViewById(R.id.txtpass).setVisibility(View.INVISIBLE);



                                        } catch (Exception ex) {
                                            Log.d("Exception", ex.toString());

                                        }


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday, picture,friends");

                        request.setParameters(parameters);
                        request.executeAsync();
                        //

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
        BoomUtils.animateView(overlay, View.GONE,0, 200);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

        //Uncomment to finish this activity and avoid going back here when the back button is pressed.
        //finish();
    }
    private void requestPassword(final JSONObject data){

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
                passTemp=m_Text;






                try {
                    data.put("password",m_Text);

                    Log.i("JSON", "onClick: " + data);
                    flag=0;
                    APICalls.post("auth/signup", data, caller);
                    Log.i("JSON", "onClick: " + data);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Name, (String) data.get("name"));
                    editor.putString(Pass, (String) data.get("password"));
                    editor.putString(Email, (String) data.get("email"));
                    editor.putString(Picture, (String) data.get("picture"));
                    editor.commit();
                    JSONObject loginObj = new JSONObject();
                    loginObj.put("email",data.get("email"));
                    loginObj.put("password",data.get("password") );
                    flag=1;
                    APICalls.post("auth/login", loginObj, caller);

                } catch (JSONException e) {
                    e.printStackTrace();
                }




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
        Log.d("Response", response.toString());
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
        BoomUtils.animateView(overlay, View.GONE,0, 200);

        switch (flag){

            case 1:
                showToastMessage(R.string.onLoginFailed, Toast.LENGTH_LONG);
            case 2:

                    disconnectFromFacebook();
                    findViewById(R.id.btnRegisterWIthEmail).setVisibility(View.VISIBLE);
                    findViewById(R.id.btnlogin).setVisibility(View.VISIBLE);
                    findViewById(R.id.txtmail).setVisibility(View.VISIBLE);
                    findViewById(R.id.txtpass).setVisibility(View.VISIBLE);

                break;

        }


    }
}
