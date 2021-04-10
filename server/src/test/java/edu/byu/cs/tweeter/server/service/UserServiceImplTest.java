package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class UserServiceImplTest {

    private UserRequest request;
    private UserResponse expectedResponse;
    private UserDAO mockUserDAO;
    private UserServiceImpl userServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup a request object to use in the tests
        request = new UserRequest("@TestUser", new AuthToken("@TestUser", "nonsenseToken"));

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new UserResponse(true, null, user1);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getUser(request.getUserAlias())).thenReturn(expectedResponse);

        userServiceImplSpy = Mockito.spy(UserServiceImpl.class);
        Mockito.when(userServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    /**
     * Verify that the {@link UserServiceImpl#getUser(UserRequest)}
     * method returns the same result as the {@link UserDAO} class.
     */
    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceImplSpy.getUser(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
