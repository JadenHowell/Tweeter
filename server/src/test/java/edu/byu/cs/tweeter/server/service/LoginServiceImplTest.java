package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class LoginServiceImplTest {

    private LoginRequest request;
    private LoginResponse expectedResponse;
    private LoginServiceImpl loginServiceImplSpy;
    private UserDAO mockUserDAO;
    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup() {
        loginServiceImplSpy = Mockito.spy(LoginServiceImpl.class);
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        mockUserDAO = Mockito.mock(UserDAO.class);

        request = new LoginRequest("@TestUser", "password");
        User user = new User("Test", "User", "@TestUser", null);
        UserResponse userResponse = new UserResponse(true, "good", user);
        Mockito.when(mockUserDAO.getUser(request.getUsername())).thenReturn(userResponse);
        Mockito.when(mockUserDAO.getHash(request.getUsername())).thenReturn("a==b");
        Mockito.when(loginServiceImplSpy.getMatch(request.getPassword(), "b", "a==")).thenReturn(true);

        AuthToken token = new AuthToken("@TestUesr", "yeaj");
        Mockito.when(mockAuthTokenDAO.newAuthToken(request.getUsername())).thenReturn(token);

        expectedResponse = new LoginResponse(user, token);

        Mockito.when(loginServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(loginServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceImplSpy.login(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertEquals(expectedResponse.getAuthToken(), response.getAuthToken());
    }
}
