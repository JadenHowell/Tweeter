package edu.byu.cs.tweeter.server.dao;

import com.password4j.Password;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class UserDAOTest {
    private static final String IMAGE_URL = "https://www.alimentarium.org/en/system/files/thumbnails/image/AL027-01_pomme_de_terre_0.jpg";
    private final User testUser = new User("Test", "User", "@TestUser", IMAGE_URL);
    private final User daoTestUser = new User("DAO", "Test", "@DAOTEST", IMAGE_URL);
    private final AuthToken fakeAuthToken = new AuthToken("@TestUser", "token");
    private UserDAO userDAO;

    @BeforeEach
    public void setup(){
        userDAO = new UserDAO();
    }

    @Test
    public void getUserSuccess() {
        UserResponse expectedResponse = new UserResponse(true, "", testUser);
        UserResponse response = userDAO.getUser("@TestUser");
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }

    @Test
    public void getUserFail() {
        UserResponse expectedResponse = new UserResponse(true, "@fakeuser not found");
        UserResponse response = userDAO.getUser("@fakeuser");
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }

    @Test
    public void getHashSuccess() {
        String password = "password";
        String hash = userDAO.getHash("@TestUser");
        String hashfromDB = hash.split("==")[0] + "==";
        String saltfromDB = hash.split("==")[1];
        boolean match = getMatch(password, saltfromDB, hashfromDB);
        Assertions.assertTrue(match);
    }

    @Test
    public void getHashFail() {
        String password = "notthepassword";
        String hash = userDAO.getHash("@TestUser");
        String hashfromDB = hash.split("==")[0] + "==";
        String saltfromDB = hash.split("==")[1];
        boolean match = getMatch(password, saltfromDB, hashfromDB);
        Assertions.assertFalse(match);
    }

    public boolean getMatch(String password, String saltFromDB, String hashFromDB){
        return Password.check(password, hashFromDB)
                .addSalt(saltFromDB)
                .withPBKDF2();
    }

    @Test
    public void putUserSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("DAO", "Test", "@DAOTest", "password");
        registerRequest.setPhotoURL(IMAGE_URL);
        userDAO.putUser(registerRequest);
        UserResponse expectedResponse = new UserResponse(true, "", daoTestUser);
        UserResponse response = userDAO.getUser("@DAOTest");
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }

    @Test
    public void returnsCorrectFollowerCount() {
        FollowerCountResponse expectedResponse = new FollowerCountResponse(true, null, 1);
        FollowerCountRequest request = new FollowerCountRequest("@ab", new AuthToken("@TestUser", "nonsenseToken"));
        FollowerCountResponse response = userDAO.getFollowerCount(request);
        Assertions.assertEquals(expectedResponse.getCount(), response.getCount());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void returnsCorrectFollowingCount() {
        FollowingCountResponse expectedResponse = new FollowingCountResponse(true, null, 1);
        FollowingCountRequest request = new FollowingCountRequest("@ab", new AuthToken("@TestUser", "nonsenseToken"));
        FollowingCountResponse response = userDAO.getFolloweeCount(request);
        Assertions.assertEquals(expectedResponse.getCount(), response.getCount());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void updateFollowCountsSuccess() {
        FollowingCountRequest request = new FollowingCountRequest("@ab", fakeAuthToken);
        FollowingCountResponse response1 = userDAO.getFolloweeCount(request);
        ChangeFollowStateRequest changeFollowStateRequest = new ChangeFollowStateRequest("@ab", "@ac", fakeAuthToken);
        ChangeFollowStateResponse changeFollowStateResponse = new ChangeFollowStateResponse(true, "", true);
        userDAO.updateFollowCounts(changeFollowStateRequest, changeFollowStateResponse);
        // revert
        changeFollowStateResponse = new ChangeFollowStateResponse(true, "", false);
        userDAO.updateFollowCounts(changeFollowStateRequest, changeFollowStateResponse);
        FollowingCountResponse expectedResponse = new FollowingCountResponse(true, null, response1.getCount());
        FollowingCountResponse response2 = userDAO.getFolloweeCount(request);
        Assertions.assertEquals(expectedResponse.getCount(), response2.getCount());
        Assertions.assertTrue(response2.isSuccess());
    }


}
