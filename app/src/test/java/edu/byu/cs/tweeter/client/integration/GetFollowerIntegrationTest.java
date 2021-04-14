package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.client.model.service.FollowingServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class GetFollowerIntegrationTest {

    private Service followerService;
    private FollowerRequest request;
    private FollowerResponse expectedResponse;

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User user2 = new User("Amy", "Ames", "@AmyAmes", MALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", "@BobBobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", "@BonnieBeatty", MALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", "@ChrisColston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", "@CindyCoats", MALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", "@DanDonaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", "@DeeDempsey", MALE_IMAGE_URL);
    private AuthToken token;
    private User user;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        user = new User("Allen", "Anderson","@AllenAnderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        LoginServiceProxy loginServiceProxy = new LoginServiceProxy();
        LoginResponse response = (LoginResponse) loginServiceProxy.serve(new LoginRequest(user.getAlias(), "password"));
        token = response.getAuthToken();
        followerService = new FollowerServiceProxy();
    }

    @Test
    public void shouldFetchResponse_when_basicRequest() throws IOException, TweeterRemoteException {
        request = new FollowerRequest("@AllenAnderson",8,null, token);
        List<User> responseUsers = Arrays.asList(user2, user3, user4, user5, user7, user8);
        expectedResponse = new FollowerResponse(responseUsers, true);
        FollowerResponse response = (FollowerResponse) followerService.serve(request);
        //can't just check if equals, because they are different instances
        boolean containsSame = true;
        for(User user : expectedResponse.getFollowers()){
            if(!response.getFollowers().contains(user)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertTrue(response.getHasMorePages());
    }

}
