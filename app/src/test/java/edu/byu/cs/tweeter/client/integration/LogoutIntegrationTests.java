package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class LogoutIntegrationTests {

    private Service logoutService;

    @Disabled
    @Test
    public void logoutIsSuccess() throws IOException, TweeterRemoteException {
        logoutService = new LogoutServiceProxy();
        LogoutResponse expectedResponse = new LogoutResponse(true, "You logged out good.");
        LogoutRequest request = new LogoutRequest("@TestUser", new AuthToken("@TestUser", "nonsenseToken"));
        LogoutResponse response = (LogoutResponse) logoutService.serve(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }
}
