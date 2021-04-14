package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowingServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class GetFollowingIntegrationTest {

    private Service followingService;
    private FollowingRequest request;
    private FollowingResponse expectedResponse;

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User user2 = new User("Amy", "Ames", "@AmyAmes", MALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", "@BobBobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", "@BonnieBeatty", MALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", "@ChrisColston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", "@CindyCoats", MALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", "@DanDonaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", "@DeeDempsey", MALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", "@ElliottEnderson", MALE_IMAGE_URL);
    private AuthToken token;
    private User user;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        user = new User("Allen", "Anderson","@AllenAnderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        LoginServiceProxy loginServiceProxy = new LoginServiceProxy();
        LoginResponse response = (LoginResponse) loginServiceProxy.serve(new LoginRequest(user.getAlias(), "password"));
        token = response.getAuthToken();
        followingService = new FollowingServiceProxy();
    }

    @Test
    public void shouldFetchResponse_when_basicRequest() throws IOException, TweeterRemoteException {
        request = new FollowingRequest(user.getAlias(),10,null, token);
        List<User> responseUsers = Arrays.asList(user2,user3,user4,user5,user6,user7,user8,user9);
        expectedResponse = new FollowingResponse(responseUsers, true);
        FollowingResponse response = (FollowingResponse) followingService.serve(request);
        //can't just check if equals, because they are different instances
        boolean containsSame = true;
        for(User user : expectedResponse.getFollowees()){
            if(!response.getFollowees().contains(user)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertTrue(response.getHasMorePages());
    }

}
