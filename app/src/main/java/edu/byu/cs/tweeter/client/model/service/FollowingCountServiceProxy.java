package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.FollowingCountService;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class FollowingCountServiceProxy extends Service implements FollowingCountService {

    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return getFollowingCount((FollowingCountRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }

    @Override
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.getFollowingCount(request);
    }
}
