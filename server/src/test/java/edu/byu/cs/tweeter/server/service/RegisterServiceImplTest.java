package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class RegisterServiceImplTest {

    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private UserDAO mockUserDAO;
    private RegisterServiceImpl registerServiceImplSpy;
    private User user;
    private AuthToken token;
    private AuthTokenDAO mockAuthTokenDao;

    @BeforeEach
    public void setup() {
        request = new RegisterRequest("first", "last", "@firstlast", "password");
        user = new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        token = new AuthToken("@TestUser", "nonsenseToken");
        expectedResponse = new RegisterResponse(user, token);
        mockUserDAO = Mockito.mock(UserDAO.class);

        Mockito.when(mockUserDAO.getUser(request.getUsername())).thenReturn(new UserResponse(true, "not found"));
        registerServiceImplSpy = Mockito.spy(RegisterServiceImpl.class);

        mockAuthTokenDao = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(registerServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDao);
        Mockito.when(mockAuthTokenDao.newAuthToken(request.getUsername())).thenReturn(token);
        Mockito.when(registerServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceImplSpy.register(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertEquals(expectedResponse.getAuthToken(), response.getAuthToken());
    }
}
