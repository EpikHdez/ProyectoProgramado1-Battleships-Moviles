package com.boom.battleships;

import android.app.Application;

import com.boom.battleships.asynctasks.APICalls;
import com.cloudinary.android.MediaManager;

/**
 * Created by erickhdez on 27/3/18.
 */

public class MyCustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MediaManager.init(getApplicationContext());
        APICalls.init(getApplicationContext());
    }
}
