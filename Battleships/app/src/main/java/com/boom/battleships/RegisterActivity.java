package com.boom.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
}
