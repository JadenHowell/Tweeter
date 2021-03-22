package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.Response;

public interface IsFollowingService {

    /**
     * Gets the follow state between users specified
     *
     * @param request contains the data required to fulfill the request.
     * @return the current follow state
     */
    Response getIsFollowing(IsFollowingRequest request)
            throws IOException, TweeterRemoteException;
}
