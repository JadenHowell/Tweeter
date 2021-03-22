package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;

public class FollowDAOTest {
    FollowDAO followDAOSpy;

    @BeforeEach
    public void setup(){
        followDAOSpy = Mockito.spy(FollowDAO.class);
        Mockito.when(followDAOSpy.getDummyState()).thenReturn(true);
    }

    @Test
    public void testIsFollowing(){
        IsFollowingRequest request = new IsFollowingRequest("@TestUser", "@OtherUser");
        Assertions.assertTrue(followDAOSpy.getIsFollowing(request).getIsFollowing());
        Assertions.assertNotNull(followDAOSpy.getIsFollowing(request));
    }

    @Test
    public void testChangeFollowState(){
        ChangeFollowStateRequest request = new ChangeFollowStateRequest("@TestUser", "@OtherUser");
        Assertions.assertTrue(followDAOSpy.changeFollowState(request).getNewFollowingState());
        Assertions.assertNotNull(followDAOSpy.changeFollowState(request));
    }
}
