package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

public class StoryDAOTest {
    private final AuthToken fakeAuthToken = new AuthToken("@TestUser", "token");
    private static final String IMAGE_URL = "https://www.alimentarium.org/en/system/files/thumbnails/image/AL027-01_pomme_de_terre_0.jpg";
    private final User testUser = new User("Test", "User", "@TestUser", IMAGE_URL);
    private StoryDAO storyDAO;

    @BeforeEach
    public void setup(){
        storyDAO = new StoryDAO();
    }

    @Test
    public void getStorySuccess() {
        StoryRequest StoryRequest = new StoryRequest("@ab", 10, null, fakeAuthToken);
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(testUser, Long.parseLong("1618200158050"), "test @ab"));
        StoryResponse expectedResponse = new StoryResponse(statuses, true);
        StoryResponse response = storyDAO.getStory(StoryRequest);
        Assertions.assertEquals(4, response.getStatusList().size());
        Assertions.assertEquals(expectedResponse.getHasMorePages(), response.getHasMorePages());
    }

    @Test
    public void getStoryFail() {
        StoryRequest StoryRequest = new StoryRequest("@ab", 10, null, fakeAuthToken);
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(null, Long.parseLong("1"), null));
        StoryResponse expectedResponse = new StoryResponse(statuses, false);
        StoryResponse response = storyDAO.getStory(StoryRequest);
        Assertions.assertNotEquals(expectedResponse.getStatusList(), response.getStatusList());
        Assertions.assertNotEquals(expectedResponse.getHasMorePages(), response.getHasMorePages());
    }

    @Test
    public void postSuccess() {
        User user = new User("New", "Boy", IMAGE_URL);
        PostRequest postRequest = new PostRequest(new Status(user, Long.parseLong("1618200158050"), "postTest"), fakeAuthToken);
        storyDAO.post(postRequest);
    }
}
