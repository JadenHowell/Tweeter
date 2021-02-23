package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.Request;
import edu.byu.cs.tweeter.model.service.response.Response;

public class LogoutService extends Service{
    @Override
    Response accessFacade(Request request) {
        return serverFacade.logout((LogoutRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }
}
