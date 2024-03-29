package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class IsFollowingServiceProxyTest {

    private IsFollowingRequest validRequest;
    private IsFollowingRequest invalidRequest;

    private IsFollowingResponse successResponse;
    private IsFollowingResponse failureResponse;

    private IsFollowingServiceProxy isFollowingServiceProxySpy;

    /**
     * Create a IsFollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        // Setup request objects to use in the tests
        validRequest = new IsFollowingRequest("@TestUser", "@OtherUser", new AuthToken("@TestUser", "nonsenseToken"));
        invalidRequest = new IsFollowingRequest(null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new IsFollowingResponse(true, null, true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getIsFollowing(validRequest)).thenReturn(successResponse);

        failureResponse = new IsFollowingResponse(false, "An exception occurred", false);
        Mockito.when(mockServerFacade.getIsFollowing(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        isFollowingServiceProxySpy = Mockito.spy(new IsFollowingServiceProxy());
        Mockito.when(isFollowingServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the IsFollowingService.serve(IsFollowingRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetIsFollowing_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = (IsFollowingResponse) isFollowingServiceProxySpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the IsFollowingService.serve(IsFollowingRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetIsFollowing_invalidRequest_returnsInvalidResponse() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = (IsFollowingResponse) isFollowingServiceProxySpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
