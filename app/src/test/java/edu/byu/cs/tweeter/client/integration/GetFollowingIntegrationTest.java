package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowingServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;

public class GetFollowingIntegrationTest {

    private Service followingService;
    private FollowingRequest request;
    private FollowingResponse expectedResponse;

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
    private final User user1 = new User("Allen", "Anderson", "@AllenAnderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", "@AmyAmes", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", "@BobBobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", "@BonnieBeatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", "@ChrisColston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", "@CindyCoats", FEMALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", "@DanDonaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", "@DeeDempsey", FEMALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", "@ElliottEnderson", MALE_IMAGE_URL);
    private final User user10 = new User("Elizabeth", "Engle", "@ElizabethEngle", FEMALE_IMAGE_URL);
    private final User user11 = new User("Frank", "Frandson", "@FrankFrandson", MALE_IMAGE_URL);
    private final User user12 = new User("Fran", "Franklin", "@FranFranklin", FEMALE_IMAGE_URL);
    private final User user13 = new User("Gary", "Gilbert", "@GaryGilbert", MALE_IMAGE_URL);
    private final User user14 = new User("Giovanna", "Giles", "@GiovannaGiles", FEMALE_IMAGE_URL);
    private final User user15 = new User("Henry", "Henderson", "@HenryHenderson", MALE_IMAGE_URL);
    private final User user16 = new User("Helen", "Hopwell", "@HelenHopwell", FEMALE_IMAGE_URL);
    private final User user17 = new User("Igor", "Isaacson", "@IgorIsaacson", MALE_IMAGE_URL);
    private final User user18 = new User("Isabel", "Isaacson", "@IsabelIsaacson", FEMALE_IMAGE_URL);
    private final User user19 = new User("Justin", "Jones", "@JustinJones", MALE_IMAGE_URL);
    private final User user20 = new User("Jill", "Johnson", "@JillJohnson", FEMALE_IMAGE_URL);

    @BeforeEach
    public void setup(){
        followingService = new FollowingServiceProxy();
    }

    @Test
    public void shouldFetchResponse_when_basicRequest() throws IOException, TweeterRemoteException {
        request = new FollowingRequest("@TestUser",10,null, new AuthToken("@TestUser", "nonsenseToken"));
        List<User> responseUsers = Arrays.asList(user1,user2,user3,user4,user5,user6,user7,user8,user9,user10);
        expectedResponse = new FollowingResponse(responseUsers, true);
        FollowingResponse response = (FollowingResponse) followingService.serve(request);
        //can't just check if equals, because they are different instances
        Assertions.assertEquals(expectedResponse.getFollowees().size(), response.getFollowees().size());
        boolean containsSame = true;
        for(User user : expectedResponse.getFollowees()){
            if(!response.getFollowees().contains(user)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertTrue(response.getHasMorePages());
    }

    @Test
    public void shouldFetchResponse_when_lastFolloweeAliasNotNull() throws IOException, TweeterRemoteException {
        request = new FollowingRequest("@TestUser",10,"@ElizabethEngle", new AuthToken("@TestUser", "nonsenseToken"));
        List<User> responseUsers = Arrays.asList(user11,user12,user13,user14,user15,user16,user17,user18,user19,user20);
        expectedResponse = new FollowingResponse(responseUsers, true);
        FollowingResponse response = (FollowingResponse) followingService.serve(request);
        //can't just check if equals, because they are different instances
        Assertions.assertEquals(expectedResponse.getFollowees().size(), response.getFollowees().size());
        boolean containsSame = true;
        for(User user : expectedResponse.getFollowees()){
            if(!response.getFollowees().contains(user)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    public void shouldFetchEmptyResponse_when_lastFolloweeAliasIsLastFollower() throws IOException, TweeterRemoteException {
        request = new FollowingRequest("@TestUser",10,"@JillJohnson", new AuthToken("@TestUser", "nonsenseToken"));
        List<User> responseUsers = Arrays.asList();
        expectedResponse = new FollowingResponse(responseUsers, true);
        FollowingResponse response = (FollowingResponse) followingService.serve(request);
        Assertions.assertEquals(expectedResponse.getFollowees().size(), response.getFollowees().size());
        Assertions.assertFalse(response.getHasMorePages());
    }
}
