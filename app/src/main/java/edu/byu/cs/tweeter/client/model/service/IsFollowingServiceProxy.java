package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.IsFollowingService;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class IsFollowingServiceProxy extends Service implements IsFollowingService {

    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return getIsFollowing((IsFollowingRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }

    @Override
    public IsFollowingResponse getIsFollowing(IsFollowingRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.getIsFollowing(request);
    }
}
