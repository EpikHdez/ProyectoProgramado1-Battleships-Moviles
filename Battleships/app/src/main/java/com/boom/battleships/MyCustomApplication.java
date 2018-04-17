package com.boom.battleships;

import android.app.Application;

import com.boom.battleships.asynctasks.APICalls;
import com.cloudinary.android.MediaManager;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by erickhdez on 27/3/18.
 */

public class MyCustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        MediaManager.init(getApplicationContext());
        APICalls.init(getApplicationContext());
    }
}
