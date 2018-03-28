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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erickhdez on 27/3/18.
 */

public class APICalls {
    private static String BASE_URL;
    private static RequestQueue queue;
    private static Map<String, String> mHeaders;

    public static void init(Context context) {
        BASE_URL = context.getResources().getString(R.string.api_url);
        mHeaders = new ArrayMap<>();
        mHeaders.put("Content-Type", "application/json");

        queue = Volley.newRequestQueue(context);
        queue.start();
    }

    /**
     * Creates a new get request, response and error, given the case, will be delivered through the
     * methods of the ApiCallRequester interface .
     *
     * @param relativeURL the url path where to do the request.
     * @param requester the requester of the call.
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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }
        };

        queue.add(request);
    }

    /**
     * Creates a new find request, response and error, given the case, will be delivered through the
     * methods of the ApiCallRequester interface .
     *
     * @param relativeURL the url path where to do the request (including id).
     * @param requester the requester of the call.
     */
    public static synchronized void find(String relativeURL, final ApiCallRequester requester) {
        final String endPoint = String.format("%s%s", BASE_URL, relativeURL);
        createJsonObjectRequest(Request.Method.GET, endPoint, null, requester);
    }

    /**
     * Creates a new post request, response and error, given the case, will be delivered through the
     * methods of the ApiCallRequester interface.
     *
     * @param relativeURL the url path where to do the request.
     * @param data the data to be send in the request.
     * @param requester the requester of the call.
     */
    public static synchronized void post(String relativeURL, JSONObject data, final ApiCallRequester requester) {
        final String endPoint = String.format("%s%s", BASE_URL, relativeURL);
        createJsonObjectRequest(Request.Method.POST, endPoint, data, requester);
    }

    /**
     * Creates a new put request, response and error, given the case, will be delivered through the
     * methods of the ApiCallRequester interface.
     *
     * @param relativeURL the url path where to do the request.
     * @param data the data to be send in the request.
     * @param requester the requester of the call.
     */
    public static synchronized void put(String relativeURL, JSONObject data, final ApiCallRequester requester) {
        final String endPoint = String.format("%s%s", BASE_URL, relativeURL);
        createJsonObjectRequest(Request.Method.PUT, endPoint, data, requester);
    }

    /**
     * Creates a new delete request, response and error, given the case, will be delivered through
     * the methods of the ApiCallRequester interface.
     *
     * @param relativeURL the url path where to do the request (including id).
     * @param requester the requester of the call.
     */
    public static synchronized void delete(String relativeURL, final ApiCallRequester requester) {
        final String endPoint = String.format("%s%s", BASE_URL, relativeURL);
        createJsonObjectRequest(Request.Method.DELETE, endPoint, null, requester);
    }

    /**
     * Creates the final request that it's going to made to the server and adds it to the Volley
     * queue.
     *
     * @param method the http verb to use.
     * @param endPoint the full api url.
     * @param data a JSONObject with the data for the server.
     * @param requester the requester of the call.
     */
    private static void createJsonObjectRequest(int method,
                                                String endPoint,
                                                JSONObject data,
                                                final ApiCallRequester requester) {
        Request request = new JsonObjectRequest(method, endPoint, data,
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
