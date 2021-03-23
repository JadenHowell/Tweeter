package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

class StatusDAOTest {

    private final User user1 = new User("Daffy", "Duck", "");
    private final User user2 = new User("Fred", "Flintstone", "");
    private final User user3 = new User("Barney", "Rubble", "");
    private final User user4 = new User("Wilma", "Rubble", "");
    private final User user5 = new User("Clint", "Eastwood", "");
    private final User user6 = new User("Mother", "Teresa", "");
    private final User user7 = new User("Harriett", "Hansen", "");
    private final User user8 = new User("Zoe", "Zabriski", "");

    private final Status status1 = new Status(user1, Calendar.getInstance().getTime().getTime(), "test1 @DaffyDuck");
    private final Status status2 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test2 @TestUser");
    private final Status status3 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test3 google.com");
    private final Status status4 = new Status(user4, Calendar.getInstance().getTime().getTime(), "A message");
    private final Status status5 = new Status(user5, Calendar.getInstance().getTime().getTime(), "post! @AmyAmes @BarneyRubble @FredFlintstone @fakeuser");
    private final Status status6 = new Status(user6, Calendar.getInstance().getTime().getTime(), "test4 @WilmaRubble");
    private final Status status7 = new Status(user7, Calendar.getInstance().getTime().getTime(), "test5 @TestUser");
    private final Status status8 = new Status(user8, Calendar.getInstance().getTime().getTime(), "test6 @TestUser");

    private StatusDAO mStatusDAOSpy;

    @BeforeEach
    void setup() {
        mStatusDAOSpy = Mockito.spy(new StatusDAO());
    }

    @Test
    void testGetFeed_noFeedForUser() {
        List<Status> statuses = Collections.emptyList();
        Mockito.when(mStatusDAOSpy.getDummyFeed(user1.getAlias())).thenReturn(statuses);

        FeedRequest request = new FeedRequest(user1.getAlias(), 10, null);
        FeedResponse response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(0, response.getStatusList().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_oneStatusForUser_limitGreaterThanUsers() {
        List<Status> statuses = Collections.singletonList(status2);
        Mockito.when(mStatusDAOSpy.getDummyFeed(user1.getAlias())).thenReturn(statuses);

        FeedRequest request = new FeedRequest(user1.getAlias(), 10, null);
        FeedResponse response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(1, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_twoStatusesForUser_limitEqualsUsers() {
        List<Status> statuses = Arrays.asList(status2, status3);
        Mockito.when(mStatusDAOSpy.getDummyFeed(user3.getAlias())).thenReturn(statuses);

        FeedRequest request = new FeedRequest(user3.getAlias(), 2, null);
        FeedResponse response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatusList() .size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertTrue(response.getStatusList().contains(status3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_limitLessThanUsers_endsOnPageBoundary() {
        List<Status> statuses = Arrays.asList(status2, status3, status4, status5, status6, status7);
        Mockito.when(mStatusDAOSpy.getDummyFeed(user5.getAlias())).thenReturn(statuses);

        FeedRequest request = new FeedRequest(user5.getAlias(), 2, null);
        FeedResponse response = mStatusDAOSpy.getFeed(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertTrue(response.getStatusList().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FeedRequest(user5.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status4));
        Assertions.assertTrue(response.getStatusList().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FeedRequest(user5.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status6));
        Assertions.assertTrue(response.getStatusList().contains(status7));
        Assertions.assertFalse(response.getHasMorePages());
    }


    @Test
    void testGetFeed_limitLessThanUsers_notEndsOnPageBoundary() {
        List<Status> statuses = Arrays.asList(status2, status3, status4, status5, status6, status7, status8);
        Mockito.when(mStatusDAOSpy.getDummyFeed(user6.getAlias())).thenReturn(statuses);

        FeedRequest request = new FeedRequest(user6.getAlias(), 2, null);
        FeedResponse response = mStatusDAOSpy.getFeed(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertTrue(response.getStatusList().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FeedRequest(user6.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status4));
        Assertions.assertTrue(response.getStatusList().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FeedRequest(user6.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status6));
        Assertions.assertTrue(response.getStatusList().contains(status7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new FeedRequest(user6.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getFeed(request);

        Assertions.assertEquals(1, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status8));
        Assertions.assertFalse(response.getHasMorePages());
    }


    /**
     * Story Testing
     *
     */

    @Test
    void testGetStory_noStoryForUser() {
        List<Status> statuses = Collections.emptyList();
        Mockito.when(mStatusDAOSpy.getDummyStory(user1.getAlias())).thenReturn(statuses);

        StoryRequest request = new StoryRequest(user1.getAlias(), 10, null);
        StoryResponse response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(0, response.getStatusList().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_oneStatusForUser_limitGreaterThanUsers() {
        List<Status> statuses = Collections.singletonList(status2);
        Mockito.when(mStatusDAOSpy.getDummyStory(user1.getAlias())).thenReturn(statuses);

        StoryRequest request = new StoryRequest(user1.getAlias(), 10, null);
        StoryResponse response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(1, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_twoStatusesForUser_limitEqualsUsers() {
        List<Status> statuses = Arrays.asList(status2, status3);
        Mockito.when(mStatusDAOSpy.getDummyStory(user3.getAlias())).thenReturn(statuses);

        StoryRequest request = new StoryRequest(user3.getAlias(), 2, null);
        StoryResponse response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(2, response.getStatusList() .size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertTrue(response.getStatusList().contains(status3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_limitLessThanUsers_endsOnPageBoundary() {
        List<Status> statuses = Arrays.asList(status2, status3, status4, status5, status6, status7);
        Mockito.when(mStatusDAOSpy.getDummyStory(user5.getAlias())).thenReturn(statuses);

        StoryRequest request = new StoryRequest(user5.getAlias(), 2, null);
        StoryResponse response = mStatusDAOSpy.getStory(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertTrue(response.getStatusList().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new StoryRequest(user5.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status4));
        Assertions.assertTrue(response.getStatusList().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new StoryRequest(user5.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status6));
        Assertions.assertTrue(response.getStatusList().contains(status7));
        Assertions.assertFalse(response.getHasMorePages());
    }


    @Test
    void testGetStory_limitLessThanUsers_notEndsOnPageBoundary() {
        List<Status> statuses = Arrays.asList(status2, status3, status4, status5, status6, status7, status8);
        Mockito.when(mStatusDAOSpy.getDummyStory(user6.getAlias())).thenReturn(statuses);

        StoryRequest request = new StoryRequest(user6.getAlias(), 2, null);
        StoryResponse response = mStatusDAOSpy.getStory(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status2));
        Assertions.assertTrue(response.getStatusList().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new StoryRequest(user6.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status4));
        Assertions.assertTrue(response.getStatusList().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new StoryRequest(user6.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(2, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status6));
        Assertions.assertTrue(response.getStatusList().contains(status7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new StoryRequest(user6.getAlias(), 2, response.getStatusList().get(1));
        response = mStatusDAOSpy.getStory(request);

        Assertions.assertEquals(1, response.getStatusList().size());
        Assertions.assertTrue(response.getStatusList().contains(status8));
        Assertions.assertFalse(response.getHasMorePages());
    }

    /**
     * Post Testing
     *
     */

    @Test
    public void testPost_returnsServiceResult() {
        PostRequest request = new PostRequest(status1);
        PostResponse response = mStatusDAOSpy.post(request);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getMessage(), "Post Successful!");
    }

}
