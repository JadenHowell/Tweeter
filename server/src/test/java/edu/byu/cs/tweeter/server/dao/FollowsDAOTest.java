package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class FollowsDAOTest {
    private final AuthToken fakeAuthToken = new AuthToken("@TestUser", "token");
    private FollowsDAO followsDAO;

    @BeforeEach
    public void setup(){
        followsDAO = new FollowsDAO();
    }

    @Test
    public void changeFollowStateSuccess() {
        ChangeFollowStateRequest changeFollowStateRequest = new ChangeFollowStateRequest("@ab", "@ac", fakeAuthToken);
        ChangeFollowStateResponse expectedResponse = new ChangeFollowStateResponse(true, null, true);
        ChangeFollowStateResponse response = followsDAO.changeFollowState(changeFollowStateRequest);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
        Assertions.assertEquals(expectedResponse.getNewFollowingState(), response.getNewFollowingState());
        //revert
        expectedResponse = new ChangeFollowStateResponse(true, null, false);
        response = followsDAO.changeFollowState(changeFollowStateRequest);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
        Assertions.assertEquals(expectedResponse.getNewFollowingState(), response.getNewFollowingState());
    }

    @Test
    public void changeFollowStateFail() {
        ChangeFollowStateRequest changeFollowStateRequest = new ChangeFollowStateRequest("@ab", "@ac", fakeAuthToken);
        ChangeFollowStateResponse expectedResponse = new ChangeFollowStateResponse(true, null, false);
        ChangeFollowStateResponse response = followsDAO.changeFollowState(changeFollowStateRequest);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
        Assertions.assertNotEquals(expectedResponse.getNewFollowingState(), response.getNewFollowingState());
        //revert
        expectedResponse = new ChangeFollowStateResponse(true, null, true);
        response = followsDAO.changeFollowState(changeFollowStateRequest);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
        Assertions.assertNotEquals(expectedResponse.getNewFollowingState(), response.getNewFollowingState());
    }

    @Test
    public void getIsFollowingSuccess() {
        IsFollowingRequest isFollowingRequest = new IsFollowingRequest("@ab", "@ac", fakeAuthToken);
        IsFollowingResponse expectedResponse = new IsFollowingResponse(true, null, false);
        IsFollowingResponse response = followsDAO.getIsFollowing(isFollowingRequest);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
        Assertions.assertEquals(expectedResponse.getIsFollowing(), response.getIsFollowing());
    }

    @Test
    public void getIsFollowingFail() {
        IsFollowingRequest isFollowingRequest = new IsFollowingRequest("@ab", "@ac", fakeAuthToken);
        IsFollowingResponse expectedResponse = new IsFollowingResponse(true, null, true);
        IsFollowingResponse response = followsDAO.getIsFollowing(isFollowingRequest);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
        Assertions.assertNotEquals(expectedResponse.getIsFollowing(), response.getIsFollowing());
    }

    @Test
    public void getFollowersSuccess() {
        FollowerRequest followerRequest = new FollowerRequest("@ab", 10, null, fakeAuthToken);
        List<String> expectedResponse = new ArrayList<>();
        expectedResponse.add("@TestUser");
        List<String> response = followsDAO.getFollowers(followerRequest);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void getFolloweesSuccess() {
        FollowingRequest followingRequest = new FollowingRequest("@ab", 10, null, fakeAuthToken);
        List<String> expectedResponse = new ArrayList<>();
        expectedResponse.add("@TestUser");
        List<String> response = followsDAO.getFollowees(followingRequest);
        Assertions.assertEquals(expectedResponse, response);
    }

}
