package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.ChangeFollowStateService;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class ChangeFollowStateServiceProxy extends Service implements ChangeFollowStateService {
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return changeFollowState((ChangeFollowStateRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }

    @Override
    public Response changeFollowState(ChangeFollowStateRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.changeFollowState(request);
    }
}
