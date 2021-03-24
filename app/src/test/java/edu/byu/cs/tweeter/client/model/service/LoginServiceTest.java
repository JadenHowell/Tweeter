package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class LoginServiceTest {
    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginServiceProxy loginServiceSpy;

    /**
     * Create a LoginServiceSpy spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        // Setup request objects to use in the tests
        validRequest = new LoginRequest("@TestUser", "password");
        invalidRequest = new LoginRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LoginResponse(new User("first3", "last3", "username", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken("@TestUser", "nonsenseToken"));
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.login(validRequest)).thenReturn(successResponse);

        failureResponse = new LoginResponse( "Login failed.");
        Mockito.when(mockServerFacade.login(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        loginServiceSpy = Mockito.spy(new LoginServiceProxy());
        Mockito.when(loginServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the LoginService.serve(LoginRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = (LoginResponse) loginServiceSpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the LoginService.serve(LoginRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_invalidRequest_returnsInvalidResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = (LoginResponse) loginServiceSpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
