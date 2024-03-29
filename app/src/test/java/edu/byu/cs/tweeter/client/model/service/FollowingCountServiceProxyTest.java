package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class FollowingCountServiceProxyTest {

    private FollowingCountRequest validRequest;
    private FollowingCountRequest invalidRequest;

    private FollowingCountResponse successResponse;
    private FollowingCountResponse failureResponse;

    private FollowingCountServiceProxy followingCountServiceProxySpy;

    /**
     * Create a FollowingCountService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        // Setup request objects to use in the tests
        validRequest = new FollowingCountRequest("@TestUser", new AuthToken("@TestUser", "nonsenseToken"));
        invalidRequest = new FollowingCountRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowingCountResponse(true, null, 7);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFollowingCount(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowingCountResponse(false, "An exception occurred", 0);
        Mockito.when(mockServerFacade.getFollowingCount(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followingCountServiceProxySpy = Mockito.spy(new FollowingCountServiceProxy());
        Mockito.when(followingCountServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the FollowingCountService.serve(FollowingCountRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowingCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowingCountResponse response = (FollowingCountResponse) followingCountServiceProxySpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the FollowingCountService.serve(FollowingCountRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowingCount_invalidRequest_returnsInvalidResponse() throws IOException, TweeterRemoteException {
        FollowingCountResponse response = (FollowingCountResponse) followingCountServiceProxySpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
