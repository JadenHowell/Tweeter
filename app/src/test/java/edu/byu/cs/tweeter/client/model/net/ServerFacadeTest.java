package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.Follow;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;

class ServerFacadeTest {

    private final User user1 = new User("Daffy", "Duck", "");
    private final User user2 = new User("Fred", "Flintstone", "");
    private final User user3 = new User("Barney", "Rubble", "");
    private final User user4 = new User("Wilma", "Rubble", "");
    private final User user5 = new User("Clint", "Eastwood", "");
    private final User user6 = new User("Mother", "Teresa", "");
    private final User user7 = new User("Harriett", "Hansen", "");
    private final User user8 = new User("Zoe", "Zabriski", "");

    private ServerFacade serverFacadeSpy;
    private ClientCommunicator mockClientCommunicator;
    private static final String FOLLOWEES_URL_PATH = "/getfollowing";

    @BeforeEach
    void setup() {
        serverFacadeSpy = Mockito.spy(new ServerFacade());
        mockClientCommunicator = Mockito.mock(ClientCommunicator.class);
        serverFacadeSpy.setClientCommunicator(mockClientCommunicator);
    }

    @Test
    void testGetFollowees_noFolloweesForUser() throws IOException, TweeterRemoteException {
        List<User> followees = Collections.emptyList();
        FollowingResponse expResp = new FollowingResponse(followees, false);
        FollowingRequest request = new FollowingRequest(user1.getAlias(), 10, null);
        Mockito.when(mockClientCommunicator.doPost(FOLLOWEES_URL_PATH, request, null, FollowingResponse.class)).thenReturn(expResp);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(0, response.getFollowees().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_oneFollowerForUser_limitGreaterThanUsers() throws IOException, TweeterRemoteException {
        List<User> followees = Collections.singletonList(user2);
        FollowingResponse expResp = new FollowingResponse(followees, false);
        FollowingRequest request = new FollowingRequest(user1.getAlias(), 10, null);
        Mockito.when(mockClientCommunicator.doPost(FOLLOWEES_URL_PATH, request, null, FollowingResponse.class)).thenReturn(expResp);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(1, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_twoFollowersForUser_limitEqualsUsers() throws IOException, TweeterRemoteException {
        List<User> followees = Arrays.asList(user2, user3);
        FollowingResponse expectedResponse = new FollowingResponse(followees, false);
        FollowingRequest request = new FollowingRequest(user3.getAlias(), 2, null);
        Mockito.when(mockClientCommunicator.doPost(FOLLOWEES_URL_PATH, request, null, FollowingResponse.class)).thenReturn(expectedResponse);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertTrue(response.getFollowees().contains(user3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_limitLessThanUsers_endsOnPageBoundary() throws IOException, TweeterRemoteException {
        List<User> followees1 = Arrays.asList(user2, user3);
        List<User> followees2 = Arrays.asList(user4, user5);
        List<User> followees3 = Arrays.asList(user6, user7);
        FollowingResponse expResp1 = new FollowingResponse(followees1, true);
        FollowingResponse expResp2 = new FollowingResponse(followees2, true);
        FollowingResponse expResp3 = new FollowingResponse(followees3, false);

        FollowingRequest request = new FollowingRequest(user5.getAlias(), 2, null);
        Mockito.when(mockClientCommunicator.doPost(FOLLOWEES_URL_PATH, request, null, FollowingResponse.class))
                .thenReturn(expResp1).thenReturn(expResp2).thenReturn(expResp3);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertTrue(response.getFollowees().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user4));
        Assertions.assertTrue(response.getFollowees().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user6));
        Assertions.assertTrue(response.getFollowees().contains(user7));
        Assertions.assertFalse(response.getHasMorePages());
    }


    @Test
    void testGetFollowees_limitLessThanUsers_notEndsOnPageBoundary() throws IOException, TweeterRemoteException {
        List<User> followees1 = Arrays.asList(user2, user3);
        List<User> followees2 = Arrays.asList(user4, user5);
        List<User> followees3 = Arrays.asList(user6, user7);
        List<User> followees4 = Arrays.asList(user8);
        FollowingResponse expResp1 = new FollowingResponse(followees1, true);
        FollowingResponse expResp2 = new FollowingResponse(followees2, true);
        FollowingResponse expResp3 = new FollowingResponse(followees3, true);
        FollowingResponse expResp4 = new FollowingResponse(followees4, false);

        FollowingRequest request = new FollowingRequest(user5.getAlias(), 2, null);
        Mockito.when(mockClientCommunicator.doPost(FOLLOWEES_URL_PATH, request, null, FollowingResponse.class))
                .thenReturn(expResp1).thenReturn(expResp2).thenReturn(expResp3).thenReturn(expResp4);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertTrue(response.getFollowees().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user4));
        Assertions.assertTrue(response.getFollowees().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user6));
        Assertions.assertTrue(response.getFollowees().contains(user7));
        Assertions.assertTrue(response.getHasMorePages());

        //Get and verify last page
        response = serverFacadeSpy.getFollowees(request);
        Assertions.assertEquals(1, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user8));
        Assertions.assertFalse(response.getHasMorePages());
    }
}
