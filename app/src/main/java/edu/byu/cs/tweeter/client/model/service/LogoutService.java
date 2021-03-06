package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class LogoutService extends Service{
    @Override
    Response accessFacade(Request request) {
        return serverFacade.logout((LogoutRequest) request);
    }

    void onSuccess(Response response) throws IOException {
    }
}
