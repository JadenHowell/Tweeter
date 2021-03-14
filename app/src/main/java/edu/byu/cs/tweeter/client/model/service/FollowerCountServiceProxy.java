package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.FollowerCountService;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class FollowerCountServiceProxy extends Service implements FollowerCountService {

    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return getFollowerCount((FollowerCountRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }

    @Override
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.getFollowerCount(request);
    }
}
