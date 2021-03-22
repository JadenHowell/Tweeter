package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.LoginService;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class LoginServiceProxy extends Service implements LoginService {
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return login((LoginRequest)request);
    }

    @Override
    void onSuccess(Response response) throws IOException {

    }

    @Override
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.login((LoginRequest) request);
    }
}
