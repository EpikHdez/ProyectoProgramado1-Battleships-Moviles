package com.boom.battleships.interfaces;

/**
 * Created by erickhdez on 27/3/18.
 */

public interface ApiCallRequester {
    /**
     * Method used in the classes that call an api endpoint for receiving their response when this
     * last one is finished.
     *
     * @param response the response from the api
     */
    void receiveApiResponse(Object response);

    /**
     * Method used in the classes that call an api endpoint for receiving the error message in case
     * the request fails.
     *
     * @param error the error description
     */
    void receiveApiError(Object error);
}
