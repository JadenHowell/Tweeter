package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;

public interface FollowerCountService {

    /**
     * Returns the number of users that the user specified in the request is followed by.
     *
     * @param request contains the data required to fulfill the request.
     * @return the number of followees.
     */
    FollowerCountResponse getFollowerCount(FollowerCountRequest request)
            throws IOException, TweeterRemoteException;
}
