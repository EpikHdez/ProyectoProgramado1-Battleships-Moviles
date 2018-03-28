package com.boom.battleships.asynctasks;

import android.content.Context;
import android.util.ArrayMap;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boom.battleships.R;
import com.boom.battleships.interfaces.ApiCallRequester;
import com.boom.battleships.interfaces.AsyncTaskRequester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erickhdez on 27/3/18.
 */

public class APICalls {
    private static String BASE_URL;
    private static RequestQueue queue;

    public static void init(Context context) {
        BASE_URL = context.getResources().getString(R.string.api_url);

        queue = Volley.newRequestQueue(context);
        queue.start();
    }

    /**
     * Creates a new get request, response and error, given the case, will be delivered through the
     * methods of the in the AsyncTaskRequester interface .
     *
     * @param relativeURL the url path where to do the request.
     * @param requester the requester of this method.
     */
    public static synchronized void get(String relativeURL, final ApiCallRequester requester) {
        final String endPoint = String.format("%s/%s", BASE_URL, relativeURL);

        Request request = new JsonArrayRequest(Request.Method.GET, endPoint, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        requester.receiveApiResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requester.receiveApiError(error);
                    }
                });

        queue.add(request);
    }

    public static synchronized void post(String relativeURL, JSONObject data, final ApiCallRequester requester) {
        final String endPoint = String.format("%s%s", BASE_URL, relativeURL);
        final Map<String, String> mHeaders = new ArrayMap<>();

        mHeaders.put("Content-Type", "application/json");

        Request request = new JsonObjectRequest(Request.Method.POST, endPoint, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requester.receiveApiResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requester.receiveApiError(error);
                    }
                }) {
            public Map<String, String> getHeaders() {
                return mHeaders;
            }
        };

        queue.add(request);
    }
}
