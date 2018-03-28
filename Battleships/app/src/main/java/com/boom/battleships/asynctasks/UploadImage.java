package com.boom.battleships.asynctasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.cloudinary.Url;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

/**
 * Created by erickhdez on 27/3/18.
 */

public class UploadImage extends AsyncTask<Uri, Void, String> {
    AsyncTaskRequester mRequester;

    public UploadImage(AsyncTaskRequester requester) {
        this.mRequester = requester;
    }

    @Override
    protected String doInBackground(Uri... uris) {
        Random random =  new Random(Calendar.getInstance().hashCode());
        String publicId = String.valueOf(random.nextInt());

        MediaManager.get().upload(uris[0]).option("public_id", publicId).dispatch();
        return MediaManager.get().url().generate(publicId);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mRequester.receiveAsyncResponse(s);
    }
}
