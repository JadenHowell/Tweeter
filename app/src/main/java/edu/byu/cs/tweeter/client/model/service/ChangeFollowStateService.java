package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class ChangeFollowStateService extends Service{
    @Override
    Response accessFacade(Request request) {
        return serverFacade.changeFollowState((ChangeFollowStateRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }
}
