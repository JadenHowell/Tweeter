package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

public class GetStoryIntegrationTest {

    private Service storyService;
    private StoryRequest request;
    private StoryResponse expectedResponse;
    private AuthToken token;
    private User user;
    private Status status;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        user = new User("h", "j","@hj", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        LoginServiceProxy loginServiceProxy = new LoginServiceProxy();
        LoginResponse response = (LoginResponse) loginServiceProxy.serve(new LoginRequest(user.getAlias(), "password"));
        token = response.getAuthToken();
        storyService = new StoryServiceProxy();
        status = new Status(user, 0, "testingStatus");
    }

    @Test
    public void shouldFetchResponse_when_basicRequest() throws IOException, TweeterRemoteException {
        request = new StoryRequest(user.getAlias(),10,null, token);
        List<Status> responseStatuses = Arrays.asList(status);
        expectedResponse = new StoryResponse(responseStatuses, false);
        StoryResponse response = (StoryResponse) storyService.serve(request);
        Assertions.assertEquals(expectedResponse.getStatusList().get(0), response.getStatusList().get(0));
        Assertions.assertTrue(response.getHasMorePages());
    }

}
