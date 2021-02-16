package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.service.request.Request;
import edu.byu.cs.tweeter.model.service.response.Response;

public class FollowingCountService extends Service {

    @Override
    Response accessFacade(Request request) {
        return serverFacade.getFollowingCount((FollowingCountRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }
}
