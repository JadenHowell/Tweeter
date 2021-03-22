package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;

public class ChangeFollowStateServiceImplTest {
    ChangeFollowStateServiceImpl changeFollowStateServiceSpy;
    ChangeFollowStateRequest request;
    ChangeFollowStateResponse expectedResponse;
    FollowDAO mockFollowDAO;

    @BeforeEach
    public void setup(){
        changeFollowStateServiceSpy = Mockito.spy(ChangeFollowStateServiceImpl.class);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        expectedResponse = new ChangeFollowStateResponse(true, null, true);
        request = new ChangeFollowStateRequest("@TestUser","@OtherUser");
        Mockito.when(mockFollowDAO.changeFollowState(request)).thenReturn(expectedResponse);
        Mockito.when(changeFollowStateServiceSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testServiceReturnsDAOData() throws IOException, TweeterRemoteException {
        ChangeFollowStateResponse response = changeFollowStateServiceSpy.changeFollowState(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
