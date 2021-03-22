package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterPresenter {

    private final View view;

    public interface View {

    }

    public RegisterPresenter(View view) {this.view = view;}

    public RegisterResponse register(RegisterRequest registerRequest) throws IOException, TweeterRemoteException {
        Service registerService = getRegisterService();
        return (RegisterResponse) registerService.serve(registerRequest);
    }

    Service getRegisterService() {
        return new RegisterServiceProxy();
    }
}
