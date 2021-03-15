package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public interface IsFollowingService {

    /**
     * Gets the follow state between users specified
     *
     * @param request contains the data required to fulfill the request.
     * @return the current follow state
     */
    IsFollowingResponse getIsFollowing(IsFollowingRequest request)
            throws IOException, TweeterRemoteException;
}
