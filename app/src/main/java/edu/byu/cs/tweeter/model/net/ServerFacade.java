package edu.byu.cs.tweeter.model.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.Response;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {
    // This is the hard coded followee/follower data returned by the 'getFollowees()'/'getFollowers()' methods
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private static final String STORY_TYPE = "story";
    private static final String FEED_TYPE = "feed";

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);

    private final Status status1 = new Status(user1, Calendar.getInstance().getTime(), "test1 @GiovannaGiles");
    private final Status status2 = new Status(user2, Calendar.getInstance().getTime(), "test2 @TestUser");
    private final Status status3 = new Status(user3, Calendar.getInstance().getTime(), "test3 google.com");
    private final Status status4 = new Status(new User("Test", "User",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), Calendar.getInstance().getTime(),
            "A really long message that should take up a few lines and has a mention to someone @AmyAmes. and a http://url.com");
    private final Status status5 = new Status(user4, Calendar.getInstance().getTime(), "post! @AmyAmes @JillJohnson @ChrisColston @fakeuser");

    /**
     * Performs a login and if successful, returns the logged in user and an auth token. The current
     * implementation is hard-coded to return a dummy user and doesn't actually make a network
     * request.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) {
        User user = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        return new LoginResponse(user, new AuthToken());
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the following response.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {

        // Used in place of assert statements because Android does not support them
        if (BuildConfig.DEBUG) {
            if (request.getLimit() < 0) {
                throw new AssertionError();
            }

            if (request.getFollowerAlias() == null) {
                throw new AssertionError();
            }
        }

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);

            for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                responseFollowees.add(allFollowees.get(followeesIndex));
            }

            hasMorePages = followeesIndex < allFollowees.size();
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees      the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if (lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if (lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
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
    public FollowerResponse getFollowers(FollowerRequest request) {

        // Used in place of assert statements because Android does not support them
        if (BuildConfig.DEBUG) {
            if (request.getLimit() < 0) {
                throw new AssertionError();
            }

            if (request.getFolloweeAlias() == null) {
                throw new AssertionError();
            }
        }

        List<User> allFollowers = getDummyFollowers();
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int followersIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);

            for (int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                responseFollowers.add(allFollowers.get(followersIndex));
            }

            hasMorePages = followersIndex < allFollowers.size();
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    /**
     * Determines the index for the first follower in the specified 'allFollowers' list that should
     * be returned in the current request. This will be the index of the next follower after the
     * specified 'lastFollower'.
     *
     * @param lastFollowerAlias the alias of the last follower that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowers      the generated list of followers from which we are returning paged results.
     * @return the index of the first follower to be returned.
     */
    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {

        int followersIndex = 0;

        if (lastFollowerAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if (lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                    break;
                }
            }
        }

        return followersIndex;
    }

    /**
     * Returns the list of dummy follower data. This is written as a separate method to allow
     * mocking of the followers.
     *
     * @return the followers.
     */
    List<User> getDummyFollowers() {
        return Arrays.asList(user2, user3, user4, user5, user7, user8, user9, user10,
                user12, user13, user14, user16, user17, user19);
    }

    /**
     * Returns a response based on the number of users this user is following
     *
     * @param request a request containing the user alias to check for
     * @return a response containing the number of users our user is following
     */
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        String userAlias = request.getFollowerAlias();

        int count;
        if (userAlias.equals("@TestUser")) {
            count = 7;
        } else {
            count = 100;
        }

        FollowingCountResponse response = new FollowingCountResponse(true, null, count);
        return response;
    }

    /**
     * Returns a response based on the number of users this user is followed by
     *
     * @param request a request containing the user alias to check for
     * @return a response containing the number of users our user is followed by
     */
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {
        String userAlias = request.getFolloweeAlias();
        int count;
        if (userAlias.equals("@TestUser")) {
            count = 30;
        } else {
            count = 3;
        }

        FollowerCountResponse response = new FollowerCountResponse(true, null, count);
        return response;
    }

    public Response getIsFollowing(IsFollowingRequest request) {
        String rootUserAlias = request.getRootUserAlias();
        String otherUserAlias = request.getOtherUserAlias();
        boolean didChanceFollow = true;
        if (Math.random() < .5) {
            didChanceFollow = false;
        }
        IsFollowingResponse response = new IsFollowingResponse(true, null, didChanceFollow);
        return response;
    }

    static boolean followState = true;

    public Response changeFollowState(ChangeFollowStateRequest request) {
        String loggedInUserAlias = request.getRootUserAlias();
        String otherUserAlias = request.getOtherUserAlias();
        ChangeFollowStateResponse response = new ChangeFollowStateResponse(true, null, followState);
        followState = !followState;
        return response;
    }


    /**
     * Returns the statuses that the user specified in the request has in their story. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the story response.
     */
    public StoryResponse getStory(StoryRequest request) {

        // Used in place of assert statements because Android does not support them
        if (BuildConfig.DEBUG) {
            if (request.getLimit() < 0) {
                throw new AssertionError();
            }

            if (request.getUserAlias() == null) {
                throw new AssertionError();
            }
        }

        List<Status> allStatuses = getDummyStory(request.getUserAlias());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int statusesIndex = getStoryStartingIndex(request.getLastStatus(), allStatuses);

            for (int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                responseStatuses.add(allStatuses.get(statusesIndex));
            }

            hasMorePages = statusesIndex < allStatuses.size();
        }

        return new StoryResponse(responseStatuses, hasMorePages);
    }

    /**
     * Determines the index for the first status in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next statuses after the
     * specified 'lastStatus'.
     *
     * @param lastStatus  the alias of the last status that was returned in the previous
     *                    request or null if there was no previous request.
     * @param allStatuses the generated list of statuses from which we are returning paged results.
     * @return the index of the first status to be returned.
     */
    private int getStoryStartingIndex(String lastStatus, List<Status> allStatuses) {

        int statusesIndex = 0;

        if (lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if (lastStatus.equals(allStatuses.get(i).getUser().getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    /**
     * Returns the list of dummy story data. This is written as a separate method to allow
     * mocking of the story.
     *
     * @return the statuses.
     */
    List<Status> getDummyStory(String userAlias) {
        List<Status> allStatus = Arrays.asList(status1, status5, status2, status3, status4, status1, status2, status3, status4);
        List<Status> returnList = new ArrayList<>();
        for (int i = 0; i < allStatus.size(); i++) {
            if (allStatus.get(i).getUser().getAlias().equals(userAlias)) {
                returnList.add(allStatus.get(i));
            }
        }
        return returnList;
    }

    /**
     * Returns the statuses that the user specified in the request has in their feed. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the feed response.
     */
    public FeedResponse getFeed(FeedRequest request) {

        // Used in place of assert statements because Android does not support them
        if (BuildConfig.DEBUG) {
            if (request.getLimit() < 0) {
                throw new AssertionError();
            }

            if (request.getUserAlias() == null) {
                throw new AssertionError();
            }
        }

        List<Status> allStatuses = getDummyFeed(request.getUserAlias());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int statusesIndex = getFeedStartingIndex(request.getLastStatus(), allStatuses);

            for (int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                responseStatuses.add(allStatuses.get(statusesIndex));
            }

            hasMorePages = statusesIndex < allStatuses.size();
        }

        return new FeedResponse(responseStatuses, hasMorePages);
    }

    /**
     * Determines the index for the first status in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next status after the
     * specified 'lastStatus'.
     *
     * @param lastStatus  the alias of the last status that was returned in the previous
     *                    request or null if there was no previous request.
     * @param allStatuses the generated list of statuses from which we are returning paged results.
     * @return the index of the first status to be returned.
     */
    private int getFeedStartingIndex(String lastStatus, List<Status> allStatuses) {

        int statusesIndex = 0;

        if (lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if (lastStatus.equals(allStatuses.get(i).getUser().getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    /**
     * Returns the list of dummy feed data. This is written as a separate method to allow
     * mocking of the statuses.
     *
     * @return the statuses.
     */

    List<Status> getDummyFeed(String userAlias) {
        return Arrays.asList(status1, status5, status2, status3, status4, status1, status2, status3, status4);
    }

    public PostResponse post(PostRequest request) {
        return new PostResponse(true, "Post Successful!");
    }

    public UserResponse getUser(UserRequest request) {
        List<User> allUsers = Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
        for (User user : allUsers) {
            if (user.getAlias().equals(request.getUserAlias())) {
                return new UserResponse(true, "User returned", user);
            }
        }
        return new UserResponse(false, "User \"" + request.getUserAlias() + "\" not found");
    }


    public RegisterResponse register(RegisterRequest request) {
        User user = new User("One", "Two", "Three");
        return new RegisterResponse(user, new AuthToken());
    }
}