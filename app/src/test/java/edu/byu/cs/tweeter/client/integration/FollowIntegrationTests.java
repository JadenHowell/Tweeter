package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowerCountServiceProxy;
import edu.byu.cs.tweeter.client.model.service.FollowingCountServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class FollowIntegrationTests {

    private Service followerCountService;
    private Service followingCountService;

    @Test
    public void returnsCorrectFollowerCount() throws IOException, TweeterRemoteException {
        followerCountService = new FollowerCountServiceProxy();
        FollowerCountResponse expectedResponse = new FollowerCountResponse(true, null, 15);
        FollowerCountRequest request = new FollowerCountRequest("@TestUser");
        FollowerCountResponse response = (FollowerCountResponse) followerCountService.serve(request);
        Assertions.assertEquals(expectedResponse.getCount(), response.getCount());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void returnsCorrectFollowingCount() throws IOException, TweeterRemoteException {
        followingCountService = new FollowingCountServiceProxy();
        FollowingCountResponse expectedResponse = new FollowingCountResponse(true, null, 20);
        FollowingCountRequest request = new FollowingCountRequest("@TestUser");
        FollowingCountResponse response = (FollowingCountResponse) followingCountService.serve(request);
        Assertions.assertEquals(expectedResponse.getCount(), response.getCount());
        Assertions.assertTrue(response.isSuccess());
    }
}
