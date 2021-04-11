package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.password4j.Hash;
import com.password4j.Password;

import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;
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

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    public UserResponse getUser(String userAlias) {
        GetItemSpec spec = new GetItemSpec()
                .withPrimaryKey(HandleAttr, userAlias);
        Table table = dynamoDB.getTable(TableName);
        Item outcome = table.getItem(spec);
        UserResponse response = null;
        if(outcome != null){  //if the outcome is null, that means the user does not exist
            response = new UserResponse(true, "",
                    new User(outcome.getString(firstAttr), outcome.getString(lastAttr), outcome.getString(HandleAttr), MALE_IMAGE_URL));
        } else {
            response = new UserResponse(true, userAlias + " not found", null);
        }
        return response;
    }

    public String getHash(String userAlias) {
        Table table = dynamoDB.getTable(TableName);
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(HandleAttr, userAlias);
        Item item = table.getItem(spec);
        return item.getString(passwordAttr);
    }

    public void putUser(RegisterRequest request) {
        Table table = dynamoDB.getTable(TableName);
        Hash hashOutput = Password.hash(request.getPassword())
                .addRandomSalt()
                .withPBKDF2();
        String hash = hashOutput.getResult();
        String salt = hashOutput.getSalt();
        String hashsalt = hash + salt;
        Item item = new Item()
                .withPrimaryKey(HandleAttr, request.getUsername())
                .withString(firstAttr, request.getFirstName())
                .withString(lastAttr, request.getLastName())
                .withString(passwordAttr, hashsalt)
                .withInt(followerCountAttr, 0)
                .withInt(followeeCountAttr, 0);
        table.putItem(item);
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

    public void updateFollowCounts(ChangeFollowStateRequest request, ChangeFollowStateResponse stateResponse){
        if(stateResponse.isSuccess()){
            Table table = dynamoDB.getTable(TableName);
            boolean isFollowing = stateResponse.getNewFollowingState();
            String rootUserAlias = request.getRootUserAlias();
            String otherUserAlias = request.getOtherUserAlias();
            int followeeCount = getFolloweeCount(new FollowingCountRequest(rootUserAlias, request.getAuthToken())).getCount();
            int followerCount = getFollowerCount(new FollowerCountRequest(otherUserAlias, request.getAuthToken())).getCount();
            if(isFollowing){
                followeeCount ++;
                followerCount ++;
            } else {
                followeeCount --;
                followerCount --;
            }
            UpdateItemSpec rootSpec = new UpdateItemSpec().withPrimaryKey(HandleAttr, rootUserAlias)
                    .withUpdateExpression("set followee_count = :r")
                    .withValueMap(new ValueMap().withInt(":r", followeeCount));
            UpdateItemSpec otherSpec = new UpdateItemSpec().withPrimaryKey(HandleAttr, otherUserAlias)
                    .withUpdateExpression("set follower_count = :r")
                    .withValueMap(new ValueMap().withInt(":r", followerCount));
            try{
                table.updateItem(rootSpec);
                table.updateItem(otherSpec);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}