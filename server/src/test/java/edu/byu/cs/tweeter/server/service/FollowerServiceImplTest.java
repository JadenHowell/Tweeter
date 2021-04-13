package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class FollowerServiceImplTest {

    private FollowerRequest request;
    private List<String> expectedResponse;
    private FollowsDAO mockFollowerDAO;
    private FollowerServiceImpl followerServiceImplSpy;
    private AuthTokenDAO mockAuthTokenDAO;
    private UserDAO mockUserDAO;
    private FollowerResponse finalResponse;
    private List<User> users;

    @BeforeEach
    public void setup() {
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);
        User currentUser = new User("FirstName", "LastName", null);

        // Setup a request object to use in the tests
        request = new FollowerRequest(currentUser.getAlias(), 3, null, token);

        // Setup a mock FollowingDAO that will return known responses
        mockFollowerDAO = Mockito.mock(FollowsDAO.class);
        expectedResponse = Arrays.asList("one","two");
        Mockito.when(mockFollowerDAO.getFollowers(request)).thenReturn(expectedResponse);

        followerServiceImplSpy = Mockito.spy(FollowerServiceImpl.class);
        Mockito.when(followerServiceImplSpy.getFollowerDAO()).thenReturn(mockFollowerDAO);

        mockUserDAO = Mockito.mock(UserDAO.class);
        User u1 = new User("1","2","e",null);
        User u2 = new User("2","3","a",null);
        UserResponse response1 = new UserResponse(true, null, u1);
        UserResponse response2 = new UserResponse(true, null, u2);
        Mockito.when(mockUserDAO.getUser("one")).thenReturn(response1);
        Mockito.when(mockUserDAO.getUser("two")).thenReturn(response2);
        users = Arrays.asList(u1, u2);
        finalResponse = new FollowerResponse(users, false);
        Mockito.when(followerServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(followerServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    /**
     * Verify that the {@link FollowerServiceImpl#getFollowers(FollowerRequest)}
     * method returns the same result as the {@link FollowsDAO} class.
     */
    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowerResponse response = followerServiceImplSpy.getFollowers(request);
        Assertions.assertEquals(finalResponse, response);
    }
}
