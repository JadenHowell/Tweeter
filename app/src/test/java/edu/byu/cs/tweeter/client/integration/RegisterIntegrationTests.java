package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterIntegrationTests {

    private Service registerService;

    @Test
    public void registerisSuccess() throws IOException, TweeterRemoteException {
        registerService = new RegisterServiceProxy();
        RegisterResponse expectedResponse = new RegisterResponse(new User("first_name", "last_name", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken("@TestUser", "nonsenseToken"));
        RegisterRequest request = new RegisterRequest("first_name", "last_name", "@TestUser", "password");
        RegisterResponse response = (RegisterResponse) registerService.serve(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
    }
}
