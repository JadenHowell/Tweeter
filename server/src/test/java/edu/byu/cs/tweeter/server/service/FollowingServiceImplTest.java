package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class FollowingServiceImplTest {

    private FollowingRequest request;
    private List<String> expectedResponse;
    private FollowingResponse finalResponse;
    private FollowsDAO mockFollowingDAO;
    private FollowingServiceImpl followingServiceImplSpy;
    private AuthTokenDAO mockAuthTokenDAO;
    private UserDAO mockUserDAO;
    List<User> users;

    @BeforeEach
    public void setup() {
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);
        User currentUser = new User("FirstName", "LastName", null);

        // Setup a request object to use in the tests
        request = new FollowingRequest(currentUser.getAlias(), 3, null, token);

        // Setup a mock FollowingDAO that will return known responses
        mockFollowingDAO = Mockito.mock(FollowsDAO.class);
        expectedResponse = Arrays.asList("one","two");
        Mockito.when(mockFollowingDAO.getFollowees(request)).thenReturn(expectedResponse);

        followingServiceImplSpy = Mockito.spy(FollowingServiceImpl.class);
        Mockito.when(followingServiceImplSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);

        mockUserDAO = Mockito.mock(UserDAO.class);
        User u1 = new User("1","2","e",null);
        User u2 = new User("2","3","a",null);
        UserResponse response1 = new UserResponse(true, null, u1);
        UserResponse response2 = new UserResponse(true, null, u2);
        Mockito.when(mockUserDAO.getUser("one")).thenReturn(response1);
        Mockito.when(mockUserDAO.getUser("two")).thenReturn(response2);
        users = Arrays.asList(u1, u2);
        finalResponse = new FollowingResponse(users, false);
        Mockito.when(followingServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(followingServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    /**
     * Verify that the {@link FollowingServiceImpl#getFollowees(FollowingRequest)}
     * method returns the same result as the {@link FollowsDAO} class.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowingResponse response = followingServiceImplSpy.getFollowees(request);
        Assertions.assertEquals(finalResponse, response);
    }
}
