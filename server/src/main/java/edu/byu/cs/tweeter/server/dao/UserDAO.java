package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class UserDAO {

    private static final String TableName = "user";
    private static final String HandleAttr = "alias";
    private static final String firstAttr = "first_name";
    private static final String lastAttr = "last_name";
    private static final String passwordAttr = "password";
    private static final String followerCountAttr = "follower_count";
    private static final String followeeCountAttr = "followee_count";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

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
    private final User testUser = new User("Test", "User", MALE_IMAGE_URL);

    public UserResponse getUser(UserRequest request) {
        List<User> allUsers = Arrays.asList(testUser, user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
        for (User user : allUsers) {
            if (user.getAlias().equals(request.getUserAlias())) {
                return new UserResponse(true, "User returned", user);
            }
        }
        return new UserResponse(true, "User \"" + request.getUserAlias() + "\" not found");
    }

    public RegisterResponse register(RegisterRequest request) {
        Table table = dynamoDB.getTable(TableName);
        Item item = new Item()
                .withPrimaryKey(HandleAttr, request.getUsername())
                .withString(firstAttr, request.getFirstName())
                .withString(lastAttr, request.getLastName())
                .withString(passwordAttr, request.getPassword())
                .withInt(followerCountAttr, 0)
                .withInt(followeeCountAttr, 0);
        table.putItem(item);
        return new RegisterResponse(new User(request.getFirstName(),request.getLastName(), MALE_IMAGE_URL), new AuthToken(request.getUsername(), "dummyToken"));
    }

    /**
     * Gets the count of users from the database that the user specified is following.
     *
     * @param request the request holding info about whose count of how many following is desired.
     * @return said count.
     */
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {
        Table table = dynamoDB.getTable(TableName);
        Item item = table.getItem(HandleAttr, request.getUserAlias());
        if(item != null)
            return new FollowerCountResponse(true, null, item.getInt(followerCountAttr));
        else
            return new FollowerCountResponse(true, null, 0);
    }

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param request the request containing info about whose count of how many following is desired.
     * @return said count.
     */
    public FollowingCountResponse getFolloweeCount(FollowingCountRequest request) {
        Table table = dynamoDB.getTable(TableName);
        Item item = table.getItem(HandleAttr, request.getUserAlias());
        if(item != null)
            return new FollowingCountResponse(true, null, item.getInt(followeeCountAttr));
        else
            return new FollowingCountResponse(true, null, 0);
    }
}
