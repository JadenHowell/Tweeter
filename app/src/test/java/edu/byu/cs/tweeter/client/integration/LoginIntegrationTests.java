package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
        LoginResponse expectedResponse = new LoginResponse(new User("Im", "followed", "@followed", "https://tweeter-prof-photos.s3-us-west-2.amazonaws.com/d3603033-cd11-4644-b7de-62dcf46de021"), new AuthToken("@followed", "nonsense"));
        LoginRequest request = new LoginRequest("@followed","password");
        LoginResponse response = (LoginResponse) loginService.serve(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }
}
