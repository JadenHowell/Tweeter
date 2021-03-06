package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class UserServiceTest {

    private UserRequest validRequest;
    private UserRequest invalidRequest;

    private UserResponse successResponse;
    private UserResponse failureResponse;

    private UserService userServiceSpy;

    /**
     * Create a UserService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new UserRequest("@TestUser");
        invalidRequest = new UserRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new UserResponse(true, null, user1);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getUser(validRequest)).thenReturn(successResponse);

        failureResponse = new UserResponse(false, "An exception occurred", null);
        Mockito.when(mockServerFacade.getUser(invalidRequest)).thenReturn(failureResponse);

        // Create a UserService instance and wrap it with a spy that will use the mock service
        userServiceSpy = Mockito.spy(new UserService());
        Mockito.when(userServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the UserService.serve(UserRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException {
        UserResponse response = (UserResponse) userServiceSpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the UserService.serve(UserRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetUser_invalidRequest_returnsInvalidResponse() throws IOException {
        UserResponse response = (UserResponse) userServiceSpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
