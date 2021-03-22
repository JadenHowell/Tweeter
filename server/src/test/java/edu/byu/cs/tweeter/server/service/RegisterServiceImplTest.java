package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.RegisterDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterServiceImplTest {

    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private RegisterDAO mockRegisterDAO;
    private RegisterServiceImpl registerServiceImplSpy;

    @BeforeEach
    public void setup() {
        request = new RegisterRequest();
        expectedResponse = new RegisterResponse(new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken());
        mockRegisterDAO = Mockito.mock(RegisterDAO.class);
        Mockito.when(mockRegisterDAO.register(request)).thenReturn(expectedResponse);
        registerServiceImplSpy = Mockito.spy(RegisterServiceImpl.class);
        Mockito.when(registerServiceImplSpy.registerMeBro()).thenReturn(mockRegisterDAO);
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceImplSpy.register(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
