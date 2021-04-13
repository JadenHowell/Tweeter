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
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class IsFollowingServiceImplTest {
    IsFollowingServiceImpl isFollowingServiceSpy;
    IsFollowingRequest request;
    IsFollowingResponse expectedResponse;
    FollowsDAO mockFollowDAO;
    AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup(){
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);
        isFollowingServiceSpy = Mockito.spy(IsFollowingServiceImpl.class);
        Mockito.when(isFollowingServiceSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
        mockFollowDAO = Mockito.mock(FollowsDAO.class);
        expectedResponse = new IsFollowingResponse(true, null, true);
        request = new IsFollowingRequest("@TestUser","@OtherUser", token);
        Mockito.when(mockFollowDAO.getIsFollowing(request)).thenReturn(expectedResponse);
        Mockito.when(isFollowingServiceSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testServiceReturnsDAOData() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = isFollowingServiceSpy.getIsFollowing(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
