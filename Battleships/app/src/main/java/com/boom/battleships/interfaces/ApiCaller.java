package com.boom.battleships.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by erickhdez on 27/3/18.
 */

public interface ApiCaller {
    /**
     * Method used in the classes that call an api endpoint for receiving their response when this
     * last one is finished.
     *
     * @param response the response from the api
     */
    void receiveApiResponse(JSONObject response);

    /**
     * Method used in the classes that call an api endpoint for receiving the error message in case
     * the request fails.
     *
     * @param error the error description
     */
    void receiveApiError(VolleyError error);
}
