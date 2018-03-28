package com.boom.battleships.aync.task;

import android.net.Uri;
import android.os.AsyncTask;

import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.cloudinary.android.MediaManager;

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
        String requestId = MediaManager.get().upload(uris[0]).dispatch();
        return requestId;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mRequester.receiveResponse(s);
    }
}
