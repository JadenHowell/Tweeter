package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;

public class FollowerServiceProxyTest {

    private FollowerRequest validRequest;
    private FollowerRequest invalidRequest;

    private FollowerResponse successResponse;
    private FollowerResponse failureResponse;

    private FollowerServiceProxy followerServiceProxySpy;

    /**
     * Create a FollowerService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowerRequest(currentUser.getAlias(), 3, null, new AuthToken("@TestUser", "nonsenseToken"));
        invalidRequest = new FollowerRequest(null, 0, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFollowers(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowerResponse("An exception occurred");
        Mockito.when(mockServerFacade.getFollowers(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followerServiceProxySpy = Mockito.spy(new FollowerServiceProxy());
        Mockito.when(followerServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the FollowerService.serve(FollowerRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowerResponse response = (FollowerResponse) followerServiceProxySpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that the FollowerService.serve(FollowerRequest) method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        FollowerResponse response = (FollowerResponse) followerServiceProxySpy.serve(validRequest);

        for(User user : response.getFollowers()) {
            Assertions.assertNotNull(user.getImageBytes());
        }
    }

    /**
     * Verify that for failed requests the FollowerService.serve(FollowerRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_invalidRequest_returnsNoFollowees() throws IOException, TweeterRemoteException {
        FollowerResponse response = (FollowerResponse) followerServiceProxySpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
