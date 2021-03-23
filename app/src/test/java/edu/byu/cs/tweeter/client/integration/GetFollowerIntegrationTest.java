package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;

public class GetFollowerIntegrationTest {

    private Service followerService;
    private FollowerRequest request;
    private FollowerResponse expectedResponse;

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
        followerService = new FollowerServiceProxy();
    }

    @Test
    public void shouldFetchResponse_when_basicRequest() throws IOException, TweeterRemoteException {
        request = new FollowerRequest("@TestUser",8,null, new AuthToken("@TestUser", "nonsenseToken"));
        List<User> responseUsers = Arrays.asList(user2, user3, user4, user5, user7, user8, user9, user10);
        expectedResponse = new FollowerResponse(responseUsers, true);
        FollowerResponse response = (FollowerResponse) followerService.serve(request);
        //can't just check if equals, because they are different instances
        Assertions.assertEquals(expectedResponse.getFollowers().size(), response.getFollowers().size());
        boolean containsSame = true;
        for(User user : expectedResponse.getFollowers()){
            if(!response.getFollowers().contains(user)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertTrue(response.getHasMorePages());
    }

    @Test
    public void shouldFetchResponse_when_lastFollowerAliasNotNull() throws IOException, TweeterRemoteException {
        request = new FollowerRequest("@TestUser",8,"@ElizabethEngle", new AuthToken("@TestUser", "nonsenseToken"));
        List<User> responseUsers = Arrays.asList(user12, user13, user14, user16, user17, user19, user20);
        expectedResponse = new FollowerResponse(responseUsers, true);
        FollowerResponse response = (FollowerResponse) followerService.serve(request);
        //can't just check if equals, because they are different instances
        Assertions.assertEquals(expectedResponse.getFollowers().size(), response.getFollowers().size());
        boolean containsSame = true;
        for(User user : expectedResponse.getFollowers()){
            if(!response.getFollowers().contains(user)){
                containsSame = false;
            }
        }
        Assertions.assertTrue(containsSame);
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    public void shouldFetchEmptyResponse_when_lastFollowerAliasIsLastFollower() throws IOException, TweeterRemoteException {
        request = new FollowerRequest("@TestUser",10,"@JillJohnson", new AuthToken("@TestUser", "nonsenseToken"));
        List<User> responseUsers = Arrays.asList();
        expectedResponse = new FollowerResponse(responseUsers, true);
        FollowerResponse response = (FollowerResponse) followerService.serve(request);
        Assertions.assertEquals(expectedResponse.getFollowers().size(), response.getFollowers().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    /*
    HOW do i make the server return a certain error code????
    @Test
    public void shouldThrowError_when_badRequest() throws IOException, TweeterRemoteException {
        request = new FollowerRequest(null,-1,null);
        Assertions.assertThrows(TweeterRemoteException.class, () -> followerService.serve(request));
    }
     */
}
