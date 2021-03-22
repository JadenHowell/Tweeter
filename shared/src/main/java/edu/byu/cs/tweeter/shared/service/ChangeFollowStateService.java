package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;

public interface ChangeFollowStateService {
    /**
     * Changes the follow state of the users specified inside the request
     *
     * @param request contains the data required to fulfill the request.
     * @return the new follow state.
     */
    ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request)
            throws IOException, TweeterRemoteException;
}
