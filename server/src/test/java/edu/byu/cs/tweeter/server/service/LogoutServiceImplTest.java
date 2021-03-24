package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.LogoutDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class LogoutServiceImplTest {

    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private LogoutDAO mockLogoutDAO;
    private LogoutServiceImpl logoutServiceImplSpy;

    @BeforeEach
    public void setup() {
        request = new LogoutRequest("@TestUser", new AuthToken("@TestUser", "nonsenseToken"));
        expectedResponse = new LogoutResponse(true, "Great work, you logged out!");
        mockLogoutDAO = Mockito.mock(LogoutDAO.class);
        Mockito.when(mockLogoutDAO.logout(request)).thenReturn(expectedResponse);
        logoutServiceImplSpy = Mockito.spy(LogoutServiceImpl.class);
        Mockito.when(logoutServiceImplSpy.logMeOutDAO()).thenReturn(mockLogoutDAO);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceImplSpy.logout(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
