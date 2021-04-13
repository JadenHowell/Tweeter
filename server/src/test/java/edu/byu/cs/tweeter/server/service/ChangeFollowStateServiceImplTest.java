package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;

public class ChangeFollowStateServiceImplTest {
    ChangeFollowStateServiceImpl changeFollowStateServiceSpy;
    ChangeFollowStateRequest request;
    ChangeFollowStateResponse expectedResponse;
    FollowsDAO mockFollowDAO;
    AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup(){
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);
        changeFollowStateServiceSpy = Mockito.spy(ChangeFollowStateServiceImpl.class);
        mockFollowDAO = Mockito.mock(FollowsDAO.class);
        expectedResponse = new ChangeFollowStateResponse(true, null, true);
        request = new ChangeFollowStateRequest("@TestUser","@OtherUser", token);
        Mockito.when(mockFollowDAO.changeFollowState(request)).thenReturn(expectedResponse);
        Mockito.when(changeFollowStateServiceSpy.getFollowsDAO()).thenReturn(mockFollowDAO);
        Mockito.when(changeFollowStateServiceSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testServiceReturnsDAOData() throws IOException, TweeterRemoteException {
        ChangeFollowStateResponse response = changeFollowStateServiceSpy.changeFollowState(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
