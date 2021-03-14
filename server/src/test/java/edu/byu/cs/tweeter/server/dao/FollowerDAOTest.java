package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;

public class FollowerDAOTest {

    private final User user1 = new User("Daffy", "Duck", "");
    private final User user2 = new User("Fred", "Flintstone", "");
    private final User user3 = new User("Barney", "Rubble", "");
    private final User user4 = new User("Wilma", "Rubble", "");
    private final User user5 = new User("Clint", "Eastwood", "");
    private final User user6 = new User("Mother", "Teresa", "");
    private final User user7 = new User("Harriett", "Hansen", "");
    private final User user8 = new User("Zoe", "Zabriski", "");

    private FollowerDAO followerDAOSpy;

    @BeforeEach
    void setup() {
        followerDAOSpy = Mockito.spy(new FollowerDAO());
    }

    @Test
    void testGetFollowers_noFollowersForUser() {
        List<User> followers = Collections.emptyList();
        Mockito.when(followerDAOSpy.getDummyFollowers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user1.getAlias(), 10, null);
        FollowerResponse response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(0, response.getFollowers().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_oneFolloweeForUser_limitGreaterThanUsers() {
        List<User> followers = Collections.singletonList(user2);
        Mockito.when(followerDAOSpy.getDummyFollowers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user1.getAlias(), 10, null);
        FollowerResponse response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(1, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_twoFolloweesForUser_limitEqualsUsers() {
        List<User> followers = Arrays.asList(user2, user3);
        Mockito.when(followerDAOSpy.getDummyFollowers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user3.getAlias(), 2, null);
        FollowerResponse response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertTrue(response.getFollowers().contains(user3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_limitLessThanUsers_endsOnPageBoundary() {
        List<User> followers = Arrays.asList(user2, user3, user4, user5, user6, user7);
        Mockito.when(followerDAOSpy.getDummyFollowers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user5.getAlias(), 2, null);
        FollowerResponse response = followerDAOSpy.getFollowers(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertTrue(response.getFollowers().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowerRequest(user5.getAlias(), 2, response.getFollowers().get(1).getAlias());
        response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user4));
        Assertions.assertTrue(response.getFollowers().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FollowerRequest(user5.getAlias(), 2, response.getFollowers().get(1).getAlias());
        response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user6));
        Assertions.assertTrue(response.getFollowers().contains(user7));
        Assertions.assertFalse(response.getHasMorePages());
    }


    @Test
    void testGetFollowers_limitLessThanUsers_notEndsOnPageBoundary() {
        List<User> followers = Arrays.asList(user2, user3, user4, user5, user6, user7, user8);
        Mockito.when(followerDAOSpy.getDummyFollowers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user6.getAlias(), 2, null);
        FollowerResponse response = followerDAOSpy.getFollowers(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertTrue(response.getFollowers().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowerRequest(user6.getAlias(), 2, response.getFollowers().get(1).getAlias());
        response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user4));
        Assertions.assertTrue(response.getFollowers().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FollowerRequest(user6.getAlias(), 2, response.getFollowers().get(1).getAlias());
        response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user6));
        Assertions.assertTrue(response.getFollowers().contains(user7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new FollowerRequest(user6.getAlias(), 2, response.getFollowers().get(1).getAlias());
        response = followerDAOSpy.getFollowers(request);

        Assertions.assertEquals(1, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user8));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetCountCorrect() {
        List<User> followers = Arrays.asList(user2, user3, user4, user5, user6, user7, user8);
        Mockito.when(followerDAOSpy.getDummyFollowers()).thenReturn(followers);

        FollowerCountRequest request = new FollowerCountRequest("@TestUser");
        FollowerCountResponse response = followerDAOSpy.getFollowerCount(request);

        Assertions.assertEquals(followers.size(), response.getCount());
    }
}
