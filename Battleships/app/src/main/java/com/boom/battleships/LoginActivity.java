package com.boom.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    //Facebook needed variables
    private CallbackManager cbManager;
    private LoginButton loginButton;

    private Button btnRegisterWithEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setUpFacebookLoginButton();

        btnRegisterWithEmail = findViewById(R.id.btnRegisterWIthEmail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cbManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpFacebookLoginButton() {
        cbManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);

        List<String> readPermissions = Arrays.asList("public_profile", "email");
        loginButton.setReadPermissions(readPermissions);

        // Callback registration
        loginButton.registerCallback(cbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //todo complete code
                btnRegisterWithEmail.setText(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }
}
