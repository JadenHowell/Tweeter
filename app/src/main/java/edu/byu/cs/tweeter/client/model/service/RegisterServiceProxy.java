package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.RegisterService;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class RegisterServiceProxy extends Service implements RegisterService {
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return register((RegisterRequest)request);
    }

    @Override
    void onSuccess(Response response) throws IOException {

    }

    @Override
    public RegisterResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.register((RegisterRequest) request);
    }
}
