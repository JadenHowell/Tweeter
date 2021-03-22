package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class LoginIntegrationTests {

    private Service loginService;

    @Test
    public void loginIsSuccess() throws IOException, TweeterRemoteException {
        loginService = new LoginServiceProxy();
        LoginResponse expectedResponse = new LoginResponse(new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken());
        LoginRequest request = new LoginRequest("username","password");
        LoginResponse response = (LoginResponse) loginService.serve(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
    }
}
