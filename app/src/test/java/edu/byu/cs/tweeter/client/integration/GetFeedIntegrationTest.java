package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class GetFeedIntegrationTest {

    private Service feedService;
    private FeedRequest request;
    private FeedResponse expectedResponse;
    private AuthToken token;
    private User user;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        user = new User("h", "j","@hj", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        LoginServiceProxy loginServiceProxy = new LoginServiceProxy();
        LoginResponse response = (LoginResponse) loginServiceProxy.serve(new LoginRequest(user.getAlias(), "password"));
        token = response.getAuthToken();
        feedService = new FeedServiceProxy();
    }

    @Test
    public void shouldFetchResponse_when_basicRequest() throws IOException, TweeterRemoteException {
        request = new FeedRequest(user.getAlias(),10,null, token);
        expectedResponse = new FeedResponse(new ArrayList<>(), false);
        FeedResponse response = (FeedResponse) feedService.serve(request);
        Assertions.assertEquals(expectedResponse.getStatusList().size(), response.getStatusList().size());
        Assertions.assertTrue(response.getHasMorePages());
    }

}
