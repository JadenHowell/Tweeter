package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterPresenter {

    private final View view;

    public interface View {

    }

    public RegisterPresenter(View view) {this.view = view;}

    public RegisterResponse register(RegisterRequest registerRequest) throws IOException {
        RegisterService registerService = new RegisterService();
        return (RegisterResponse) registerService.serve(registerRequest);
    }
}
