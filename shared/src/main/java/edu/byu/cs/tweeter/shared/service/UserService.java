package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.Response;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

/**
 * Defines the interface for the 'getUser' service.
 */
public interface UserService {

    /**
     * Returns the user of the user alias specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the user.
     */
    Response getUser(UserRequest request)
            throws IOException, TweeterRemoteException;
}