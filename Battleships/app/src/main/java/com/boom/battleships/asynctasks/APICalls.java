package com.boom.battleships.asynctasks;

import android.content.Context;
import android.util.ArrayMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.boom.battleships.R;
import com.boom.battleships.interfaces.ApiCaller;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erickhdez on 27/3/18.
 */

public class APICalls {
    private static String BASE_URL;
    private static RequestQueue queue;
    private static Map<String, String> headers;

    public static void init(Context context) {
        BASE_URL = "https://boombattleships.herokuapp.com/api/v1/";
        headers = new HashMap<>();

        addHeader("Content-Type", "application/json");
        addHeader("Accept", "application/json");
        queue = Volley.newRequestQueue(context);
        queue.start();
    }

    /**
     * Creates a new get request, response and error, given the case, will be delivered through the
     * methods of the ApiCaller interface .
     *
     * @param relativeURL the url path where to do the request.
     * @param caller the caller of the call.
     */
    public static synchronized void get(String relativeURL, final ApiCaller caller) {
        createJsonObjectRequest(Request.Method.GET, relativeURL, null, caller);
    }

    /**
     * Creates a new find request, response and error, given the case, will be delivered through the
     * methods of the ApiCaller interface .
     *
     * @param relativeURL the url path where to do the request (including id).
     * @param caller the caller of the call.
     */
    public static synchronized void find(String relativeURL, final ApiCaller caller) {
        createJsonObjectRequest(Request.Method.GET, relativeURL, null, caller);
    }

    /**
     * Creates a new post request, response and error, given the case, will be delivered through the
     * methods of the ApiCaller interface.
     *
     * @param relativeURL the url path where to do the request.
     * @param data the data to be send in the request.
     * @param caller the caller of the call.
     */
    public static synchronized void post(String relativeURL, JSONObject data, final ApiCaller caller) {
        createJsonObjectRequest(Request.Method.POST, relativeURL, data, caller);
    }

    /**
     * Creates a new put request, response and error, given the case, will be delivered through the
     * methods of the ApiCaller interface.
     *
     * @param relativeURL the url path where to do the request.
     * @param data the data to be send in the request.
     * @param caller the caller of the call.
     */
    public static synchronized void put(String relativeURL, JSONObject data, final ApiCaller caller) {
        createJsonObjectRequest(Request.Method.PUT, relativeURL, data, caller);
    }

    /**
     * Creates a new delete request, response and error, given the case, will be delivered through
     * the methods of the ApiCaller interface.
     *
     * @param relativeURL the url path where to do the request (including id).
     * @param caller the caller of the call.
     */
    public static synchronized void delete(String relativeURL, final ApiCaller caller) {
        createJsonObjectRequest(Request.Method.DELETE, relativeURL, null, caller);
    }

    /**
     * Adds a new header to append in the requests made to the server.
     *
     * @param key the access name.
     * @param value the value related to the key.
     */
    private static synchronized void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Creates the final request that it's going to made to the server and adds it to the Volley
     * queue.
     *
     * @param method the http verb to use.
     * @param relativeURL the relative api url.
     * @param data a JSONObject with the data for the server.
     * @param caller the caller of the call.
     */
    private static void createJsonObjectRequest(int method,
                                                String relativeURL,
                                                JSONObject data,
                                                final ApiCaller caller) {
        final String endPoint = String.format("%s%s", BASE_URL, relativeURL);

        Request request = new JsonObjectRequest(method, endPoint, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("auth_token")) {
                            String authToken = response.optString("auth_token");
                            addHeader("Authorization", authToken);
                        }


                        caller.receiveApiResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        caller.receiveApiError(error);
                    }
                }) {
                    public Map<String, String> getHeaders() {
                return headers;
            }
                };

        queue.add(request);
    }
}
