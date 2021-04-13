package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class FollowingCountServiceImplTest {
    private FollowingCountRequest request;
    private FollowingCountResponse expectedResponse;
    private UserDAO mockUserDAO;
    private FollowingCountServiceImpl followingCountServiceImplSpy;
    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup(){
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);
        request = new FollowingCountRequest("@TestUser", token);
        mockUserDAO = Mockito.mock(UserDAO.class);
        expectedResponse = new FollowingCountResponse(true, null, 10);
        Mockito.when(mockUserDAO.getFolloweeCount(request)).thenReturn(expectedResponse);

        followingCountServiceImplSpy = Mockito.spy(FollowingCountServiceImpl.class);
        Mockito.when(followingCountServiceImplSpy.getFollowingDAO()).thenReturn(mockUserDAO);
        Mockito.when(followingCountServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void should_returnDAOResponse(){
        FollowingCountResponse response = followingCountServiceImplSpy.getFollowingCount(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
