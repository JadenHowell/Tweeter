package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    private static final String SERVER_URL = "https://hyms0dv7ol.execute-api.us-west-2.amazonaws.com/test";
    private static final String FOLLOWEES_URL_PATH = "/getfollowing";
    private static final String LOGIN_URL_PATH = "/login";
    private static final String LOGOUT_URL_PATH = "/logout";
    private static final String REGISTER_URL_PATH = "/register";
    private static final String FOLLOWERS_URL_PATH = "/getfollower";
    private static final String FOLLOWER_COUNT_URL_PATH = "/getfollowercount";
    private static final String FOLLOWING_COUNT_URL_PATH = "/getfollowingcount";
    private static final String IS_FOLLOWING_URL_PATH = "/getisfollowing";
    private static final String CHANGE_FOLLOW_STATE_URL_PATH = "/changefollowstate";
    private static final String STORY_URL_PATH = "/getstory";
    private static final String FEED_URL_PATH = "/getfeed";
    private static final String USER_URL_PATH = "/getuser";
    private static final String POST_URL_PATH = "/post";

    private ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    // This is the hard coded followee/follower data returned by the 'getFollowees()'/'getFollowers()' methods
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";


    /**
     * Performs a login and if successful, returns the logged in user and an auth token. The current
     * implementation is hard-coded to return a dummy user and doesn't actually make a network
     * request.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        LoginResponse response = clientCommunicator.doPost(LOGIN_URL_PATH, request, null, LoginResponse.class);
        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Performs a logout and if successful, returns the success. The current
     * implementation is hard-coded to succeed and doesn't actually make a network
     * request.
     *
     * @param request contains all information needed to perform a logout.
     * @return the logout response.
     */
    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        LogoutResponse response = clientCommunicator.doPost(LOGOUT_URL_PATH, request, null, LogoutResponse.class);
        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request)
            throws IOException, TweeterRemoteException {
        FollowingResponse response = clientCommunicator.doPost(FOLLOWEES_URL_PATH, request, null, FollowingResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the users that the user specified in the request is followed by. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the following response.
     */
    public FollowerResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        FollowerResponse response = clientCommunicator.doPost(FOLLOWERS_URL_PATH, request, null, FollowerResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns a response based on the number of users this user is following
     *
     * @param request a request containing the user alias to check for
     * @return a response containing the number of users our user is following
     */
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) throws IOException, TweeterRemoteException {
        FollowingCountResponse response = clientCommunicator.doPost(FOLLOWING_COUNT_URL_PATH, request, null, FollowingCountResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns a response based on the number of users this user is followed by
     *
     * @param request a request containing the user alias to check for
     * @return a response containing the number of users our user is followed by
     */
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) throws IOException, TweeterRemoteException {
        FollowerCountResponse response = clientCommunicator.doPost(FOLLOWER_COUNT_URL_PATH, request, null, FollowerCountResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public IsFollowingResponse getIsFollowing(IsFollowingRequest request) throws IOException, TweeterRemoteException {
        IsFollowingResponse response = clientCommunicator.doPost(IS_FOLLOWING_URL_PATH, request, null, IsFollowingResponse.class);

        if(response.isSuccess()){
            return response;
        } else{
            throw new RuntimeException(response.getMessage());
        }
    }

    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request) throws IOException, TweeterRemoteException {
        ChangeFollowStateResponse response = clientCommunicator.doPost(CHANGE_FOLLOW_STATE_URL_PATH, request, null, ChangeFollowStateResponse.class);

        if(response.isSuccess()){
            return response;
        } else{
            throw new RuntimeException(response.getMessage());
        }
    }

    public StoryResponse getStory(StoryRequest request)
            throws IOException, TweeterRemoteException {
        StoryResponse response = clientCommunicator.doPost(STORY_URL_PATH, request, null, StoryResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public FeedResponse getFeed(FeedRequest request)
            throws IOException, TweeterRemoteException {
        FeedResponse response = clientCommunicator.doPost(FEED_URL_PATH, request, null, FeedResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public PostResponse post(PostRequest request)
            throws IOException, TweeterRemoteException {
        PostResponse response = clientCommunicator.doPost(POST_URL_PATH, request, null, PostResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public UserResponse getUser(UserRequest request)
            throws IOException, TweeterRemoteException {
        UserResponse response = clientCommunicator.doPost(USER_URL_PATH, request, null, UserResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        RegisterResponse response = clientCommunicator.doPost(REGISTER_URL_PATH, request, null, RegisterResponse.class);
        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }


    /**
     * This function exists solely to allow us to use mock client communicators, should not be used except in tests
     */
    public void setClientCommunicator(ClientCommunicator clientCommunicator){
        this.clientCommunicator = clientCommunicator;
    }

}