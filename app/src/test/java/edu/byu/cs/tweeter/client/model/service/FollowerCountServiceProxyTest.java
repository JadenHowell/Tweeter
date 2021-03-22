package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;

public class FollowerCountServiceProxyTest {

    private FollowerCountRequest validRequest;
    private FollowerCountRequest invalidRequest;

    private FollowerCountResponse successResponse;
    private FollowerCountResponse failureResponse;

    private FollowerCountServiceProxy followerCountServiceProxySpy;

    /**
     * Create a FollowerCountService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        // Setup request objects to use in the tests
        validRequest = new FollowerCountRequest("@TestUser");
        invalidRequest = new FollowerCountRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowerCountResponse(true, null, 7);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFollowerCount(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowerCountResponse(false, "An exception occurred", 0);
        Mockito.when(mockServerFacade.getFollowerCount(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followerCountServiceProxySpy = Mockito.spy(new FollowerCountServiceProxy());
        Mockito.when(followerCountServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the FollowerCountService.serve(FollowerCountRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowerCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowerCountResponse response = (FollowerCountResponse) followerCountServiceProxySpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the FollowerCountService.serve(FollowerCountRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowerCount_invalidRequest_returnsInvalidResponse() throws IOException, TweeterRemoteException {
        FollowerCountResponse response = (FollowerCountResponse) followerCountServiceProxySpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
