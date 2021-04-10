package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class FollowsDAO {
    // This is the hard coded follower data returned by the 'getFollowers()' method
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

    /**
     * changes the follow state between two specified users
     * @param request contains the users of whom to change the state of
     * @return the new follow state
     */
    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request){
        //TODO: replace with actual implementation
        ChangeFollowStateResponse response = new ChangeFollowStateResponse(true, null, getDummyState());
        return response;
    }

    /**
     * returns the state of following between two specified users
     * @param request contains the data of which users to check state between
     * @return the current state of following or not
     */
    public IsFollowingResponse getIsFollowing(IsFollowingRequest request){
        //TODO: replace with actual implementation
        IsFollowingResponse response = new IsFollowingResponse(true, null, getDummyState());
        return response;
    }

    /**
     * exists for the purpose of mocking tests
     * @return randomly returns true or false
     */
    public boolean getDummyState(){
        return Math.random() > .5;
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getFolloweeAlias() != null;

        List<User> allFollowers = getDummyFollowers();
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followersIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
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
     * @param allFollowers the generated list of followers from which we are returning paged results.
     * @return the index of the first follower to be returned.
     */
    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {

        int followersIndex = 0;

        if(lastFollowerAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
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
                user12, user13, user14, user16, user17, user19, user20);
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
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
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
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
}
