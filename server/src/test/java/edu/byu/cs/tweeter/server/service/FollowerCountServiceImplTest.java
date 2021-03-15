package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;

public class FollowerCountServiceImplTest {
    private FollowerCountRequest request;
    private FollowerCountResponse expectedResponse;
    private FollowerDAO mockFollowerDAO;
    private FollowerCountServiceImpl followerCountServiceImplSpy;

    @BeforeEach
    public void setup(){
        request = new FollowerCountRequest("@TestUser");
        mockFollowerDAO = Mockito.mock(FollowerDAO.class);
        expectedResponse = new FollowerCountResponse(true, null, 10);
        Mockito.when(mockFollowerDAO.getFollowerCount(request)).thenReturn(expectedResponse);

        followerCountServiceImplSpy = Mockito.spy(FollowerCountServiceImpl.class);
        Mockito.when(followerCountServiceImplSpy.getFollowerDAO()).thenReturn(mockFollowerDAO);
    }

    @Test
    public void should_returnDAOResponse(){
        FollowerCountResponse response = followerCountServiceImplSpy.getFollowerCount(request);
        Assertions.assertEquals(expectedResponse,response);
    }
}
