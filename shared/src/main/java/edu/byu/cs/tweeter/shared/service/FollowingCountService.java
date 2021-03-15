package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public interface FollowingCountService {

    /**
     * Returns the number of users that the user specified in the request is following.
     *
     * @param request contains the data required to fulfill the request.
     * @return the number of following.
     */
    FollowingCountResponse getFollowingCount(FollowingCountRequest request)
            throws IOException, TweeterRemoteException;
}
