package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.server.service.FollowerCountServiceImpl;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;

public class FollowerDAO {
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
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param request the request holding info about whose count of how many following is desired.
     * @return said count.
     */
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        String userAlias = request.getFolloweeAlias();
        FollowerCountResponse response = new FollowerCountResponse(true, null, getDummyFollowers().size());
        return response;
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
}
