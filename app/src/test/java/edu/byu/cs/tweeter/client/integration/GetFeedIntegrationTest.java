package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;

public class GetFeedIntegrationTest {

    private Service feedService;
    private FeedRequest request;
    private FeedResponse expectedResponse;

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User testUser = new User("Test", "User", MALE_IMAGE_URL);

    private final Status status1 = new Status(user1, Calendar.getInstance().getTime().getTime(), "test1 @AllenAnderson");
    private final Status status2 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test2 @TestUser");
    private final Status status3 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test3 google.com");
    private final Status status4 = new Status(testUser, Calendar.getInstance().getTime().getTime(),
            "A really long message that should take up a few lines and has a mention to someone @AmyAmes. and a http://url.com");
    private final Status status5 = new Status(user4, Calendar.getInstance().getTime().getTime(), "post! @AmyAmes @BobBobson @ChrisColston @fakeuser");
    private final Status status6 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test4 @BonnieBeatty");
    private final Status status7 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test5 @TestUser");
    private final Status status8 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test6 @TestUser");
    private final Status status9 = new Status(user1, Calendar.getInstance().getTime().getTime(), "test7 @GiovannaGiles");
    private final Status status10 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test8 @TestUser");
    private final Status status11 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test9 google.com");
    private final Status status12 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test10");
    private final Status status13 = new Status(user4, Calendar.getInstance().getTime().getTime(), "test11");
    private final Status status14 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test12 @GiovannaGiles");
    private final Status status15 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test13 @TestUser");
    private final Status status16 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test14 @GiovannaGiles");
    private final Status status17 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test15 @TestUser");
    private final Status status18 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test16 @TestUser");
    private final Status status19 = new Status(user1, Calendar.getInstance().getTime().getTime(), "test17 @GiovannaGiles");
    private final Status status20 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test18 @TestUser");
    private final Status status21 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test19 google.com");
    private final Status status22 = new Status(testUser, Calendar.getInstance().getTime().getTime(), "test20");
    private final Status status23 = new Status(user4, Calendar.getInstance().getTime().getTime(), "test21");
    private final Status status24 = new Status(user2, Calendar.getInstance().getTime().getTime(), "test22 @GiovannaGiles");
    private final Status status25 = new Status(user3, Calendar.getInstance().getTime().getTime(), "test23 @TestUser");

    @BeforeEach
    public void setup(){
        feedService = new FeedServiceProxy();
    }

    @Disabled
    @Test
    public void shouldFetchResponse_when_basicRequest() throws IOException, TweeterRemoteException {
        request = new FeedRequest("@TestUser",10,null, new AuthToken("@TestUser", "nonsenseToken"));
        List<Status> responseStatuses = Arrays.asList(status1,status2,status3,status4,status5,status6,status7,status8,status9,status10);
        expectedResponse = new FeedResponse(responseStatuses, true);
        FeedResponse response = (FeedResponse) feedService.serve(request);
        //can't just check if equals, because they are different instances
        Assertions.assertEquals(expectedResponse.getStatusList().size(), response.getStatusList().size());
        boolean containsSame = true;
        for(Status status : expectedResponse.getStatusList()){
            if(!response.getStatusList().contains(status)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertTrue(response.getHasMorePages());
    }

    @Disabled
    @Test
    public void shouldFetchResponse_when_lastFolloweeAliasNotNull() throws IOException, TweeterRemoteException {
        request = new FeedRequest("@TestUser",10, status15, new AuthToken("@TestUser", "nonsenseToken"));
        List<Status> responseStatuses = Arrays.asList(status16,status17,status18,status19,status20,status21,status22,status23,status24,status25);
        expectedResponse = new FeedResponse(responseStatuses, true);
        FeedResponse response = (FeedResponse) feedService.serve(request);
        //can't just check if equals, because they are different instances
        Assertions.assertEquals(expectedResponse.getStatusList().size(), response.getStatusList().size());
        boolean containsSame = true;
        for(Status status : expectedResponse.getStatusList()){
            if(!response.getStatusList().contains(status)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Disabled
    @Test
    public void shouldFetchEmptyResponse_when_lastFolloweeAliasIsLastFollower() throws IOException, TweeterRemoteException {
        request = new FeedRequest("@TestUser",10, status25, new AuthToken("@TestUser", "nonsenseToken"));
        List<Status> responseStatuses = Arrays.asList();
        expectedResponse = new FeedResponse(responseStatuses, true);
        FeedResponse response = (FeedResponse) feedService.serve(request);
        Assertions.assertEquals(expectedResponse.getStatusList().size(), response.getStatusList().size());
        Assertions.assertFalse(response.getHasMorePages());
    }
}
