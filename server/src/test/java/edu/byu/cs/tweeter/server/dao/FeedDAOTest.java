package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;

public class FeedDAOTest {
    private final AuthToken fakeAuthToken = new AuthToken("@TestUser", "token");
    private static final String IMAGE_URL = "https://www.alimentarium.org/en/system/files/thumbnails/image/AL027-01_pomme_de_terre_0.jpg";
    private final User testUser = new User("Test", "User", "@TestUser", IMAGE_URL);
    private FeedDAO feedDAO;

    @BeforeEach
    public void setup(){
        feedDAO = new FeedDAO();
    }

    @Test
    public void getFeedSuccess() {
        FeedRequest feedRequest = new FeedRequest("@ab", 10, null, fakeAuthToken);
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(testUser, Long.parseLong("1618200158050"), "test @ab"));
        FeedResponse expectedResponse = new FeedResponse(statuses, true);
        FeedResponse response = feedDAO.getFeed(feedRequest);
        Assertions.assertEquals(expectedResponse.getStatusList(), response.getStatusList());
        Assertions.assertEquals(expectedResponse.getHasMorePages(), response.getHasMorePages());
    }

    @Test
    public void getFeedFail() {
        FeedRequest feedRequest = new FeedRequest("@ab", 10, null, fakeAuthToken);
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(null, Long.parseLong("1"), null));
        FeedResponse expectedResponse = new FeedResponse(statuses, false);
        FeedResponse response = feedDAO.getFeed(feedRequest);
        Assertions.assertNotEquals(expectedResponse.getStatusList(), response.getStatusList());
        Assertions.assertNotEquals(expectedResponse.getHasMorePages(), response.getHasMorePages());
    }

    @Test
    public void postToFeedSuccess() {
        List<String> followers = new ArrayList<>();
        followers.add("@TestUser");
        feedDAO.postToFeed(followers, "@newboy", "postTest", "1618200158050");
    }
}
