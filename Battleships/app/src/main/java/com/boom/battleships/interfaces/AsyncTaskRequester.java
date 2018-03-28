package com.boom.battleships.interfaces;

/**
 * Created by erickhdez on 27/3/18.
 */

/**
 * This interface must be implemented by every class that needs to to async tasks.
 */
public interface AsyncTaskRequester {
    /**
     * Method used in the classes that call an async task for receiving their response with this
     * last one is finished.
     *
     * @param response the response from the async task
     */
    void receiveResponse(Object response);
}
