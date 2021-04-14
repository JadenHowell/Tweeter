package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterIntegrationTests {

    private Service registerService;
    private User user;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        user = new User("h", "j","@hj", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        registerService = new RegisterServiceProxy();
    }

    @Test
    public void registerisSuccess() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest("h", "j", "@hj", "password");
        RegisterResponse response = (RegisterResponse) registerService.serve(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("Username already exists, pick a new one or login.", response.getMessage());
    }
}
