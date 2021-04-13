package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;

public class FollowerCountServiceImplTest {
    private FollowerCountRequest request;
    private FollowerCountResponse expectedResponse;
    private UserDAO mockUserDAO;
    private FollowerCountServiceImpl followerCountServiceImplSpy;
    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup(){
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);
        request = new FollowerCountRequest("@TestUser", token);
        mockUserDAO = Mockito.mock(UserDAO.class);
        expectedResponse = new FollowerCountResponse(true, null, 10);
        Mockito.when(mockUserDAO.getFollowerCount(request)).thenReturn(expectedResponse);

        followerCountServiceImplSpy = Mockito.spy(FollowerCountServiceImpl.class);
        Mockito.when(followerCountServiceImplSpy.getFollowerDAO()).thenReturn(mockUserDAO);
        Mockito.when(followerCountServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void should_returnDAOResponse(){
        FollowerCountResponse response = followerCountServiceImplSpy.getFollowerCount(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
