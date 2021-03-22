package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.Response;

/**
 * Defines the interface for the 'getUser' service.
 */
public interface PostService {

    /**
     * Returns the user of the user alias specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the user.
     */
    Response post(PostRequest request)
            throws IOException, TweeterRemoteException;
}