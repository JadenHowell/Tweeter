package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.service.FollowingCountService;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class FollowingCountServiceImplTest {
    private FollowingCountRequest request;
    private FollowingCountResponse expectedResponse;
    private FollowingDAO mockFollowingDAO;
    private FollowingCountServiceImpl followingCountServiceImplSpy;

    @BeforeEach
    public void setup(){
        request = new FollowingCountRequest("@TestUser", new AuthToken("@TestUser", "nonsenseToken"));
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        expectedResponse = new FollowingCountResponse(true, null, 10);
        Mockito.when(mockFollowingDAO.getFolloweeCount(request)).thenReturn(expectedResponse);

        followingCountServiceImplSpy = Mockito.spy(FollowingCountServiceImpl.class);
        Mockito.when(followingCountServiceImplSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);
    }

    @Test
    public void should_returnDAOResponse(){
        FollowingCountResponse response = followingCountServiceImplSpy.getFollowingCount(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
