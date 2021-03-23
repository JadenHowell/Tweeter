package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class IsFollowingServiceImplTest {
    IsFollowingServiceImpl isFollowingServiceSpy;
    IsFollowingRequest request;
    IsFollowingResponse expectedResponse;
    FollowDAO mockFollowDAO;

    @BeforeEach
    public void setup(){
        isFollowingServiceSpy = Mockito.spy(IsFollowingServiceImpl.class);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        expectedResponse = new IsFollowingResponse(true, null, true);
        request = new IsFollowingRequest("@TestUser","@OtherUser", new AuthToken("@TestUser", "nonsenseToken"));
        Mockito.when(mockFollowDAO.getIsFollowing(request)).thenReturn(expectedResponse);
        Mockito.when(isFollowingServiceSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testServiceReturnsDAOData() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = isFollowingServiceSpy.getIsFollowing(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
