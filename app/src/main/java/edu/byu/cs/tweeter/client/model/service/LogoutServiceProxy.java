package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.LogoutService;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class LogoutServiceProxy extends Service implements LogoutService {
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return logout((LogoutRequest)request);
    }

    @Override
    void onSuccess(Response response) throws IOException {

    }

    @Override
    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.logout((LogoutRequest) request);
    }
}
