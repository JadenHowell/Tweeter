package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterServiceTest {
    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private RegisterResponse successResponse;
    private RegisterResponse failureResponse;

    private RegisterService registerServiceSpy;

    /**
     * Create a RegisterServiceSpy spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        // Setup request objects to use in the tests
        validRequest = new RegisterRequest("first", "last", "username", "password");
        invalidRequest = new RegisterRequest(null, null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new RegisterResponse(new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken());
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.register(validRequest)).thenReturn(successResponse);

        failureResponse = new RegisterResponse( "Failed to register new user.");
        Mockito.when(mockServerFacade.register(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        registerServiceSpy = Mockito.spy(new RegisterService());
        Mockito.when(registerServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the RegisterService.serve(RegisterRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testRegister_validRequest_correctResponse() throws IOException {
        RegisterResponse response = (RegisterResponse) registerServiceSpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the RegisterService.serve(RegisterRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testRegister_invalidRequest_returnsInvalidResponse() throws IOException {
        RegisterResponse response = (RegisterResponse) registerServiceSpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
