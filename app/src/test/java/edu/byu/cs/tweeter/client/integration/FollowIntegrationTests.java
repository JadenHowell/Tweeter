package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowerCountServiceProxy;
import edu.byu.cs.tweeter.client.model.service.FollowingCountServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class FollowIntegrationTests {

    private Service followerCountService;
    private Service followingCountService;
    private AuthToken token;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        LoginServiceProxy loginServiceProxy = new LoginServiceProxy();
        LoginResponse response = (LoginResponse) loginServiceProxy.serve(new LoginRequest("@followed", "password"));
        token = response.getAuthToken();
    }

    @Test
    public void returnsCorrectFollowerCount() throws IOException, TweeterRemoteException {
        followerCountService = new FollowerCountServiceProxy();
        FollowerCountResponse expectedResponse = new FollowerCountResponse(true, null, 10000);
        FollowerCountRequest request = new FollowerCountRequest("@followed", token);
        FollowerCountResponse response = (FollowerCountResponse) followerCountService.serve(request);
        Assertions.assertEquals(expectedResponse.getCount(), response.getCount());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void returnsCorrectFollowingCount() throws IOException, TweeterRemoteException {
        followingCountService = new FollowingCountServiceProxy();
        FollowingCountResponse expectedResponse = new FollowingCountResponse(true, null, 0);
        FollowingCountRequest request = new FollowingCountRequest("@followed", token);
        FollowingCountResponse response = (FollowingCountResponse) followingCountService.serve(request);
        Assertions.assertEquals(expectedResponse.getCount(), response.getCount());
        Assertions.assertTrue(response.isSuccess());
    }
}
