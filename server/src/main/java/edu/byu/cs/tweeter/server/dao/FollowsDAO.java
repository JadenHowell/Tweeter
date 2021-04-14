package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class FollowsDAO {
    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2")
            .build();
    private DynamoDB dynamoDB = new DynamoDB(client);
    private Table table = dynamoDB.getTable("follows");

    private String PRIMARY_KEY = "follower_handle";
    private String SORT_KEY = "followee_handle";

    /**
     * changes the follow state between two specified users
     * @param request contains the users of whom to change the state of
     * @return the new follow state
     */
    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request){
        //first, need to find out if they are following, then do opposite.
        IsFollowingRequest query = new IsFollowingRequest(request.getRootUserAlias(), request.getOtherUserAlias(), request.getAuthToken());
        IsFollowingResponse answer = getIsFollowing(query);
        boolean currentlyFollowing = answer.getIsFollowing();
        ChangeFollowStateResponse response = null;
        if(currentlyFollowing){
            response = stopFollowing(request);
        } else {
            response = startFollowing(request);
        }

        return response;
    }

    private ChangeFollowStateResponse stopFollowing(ChangeFollowStateRequest request){
        DeleteItemSpec spec = new DeleteItemSpec()
                .withPrimaryKey(PRIMARY_KEY, request.getRootUserAlias(), SORT_KEY, request.getOtherUserAlias());
        DeleteItemOutcome outcome = null;
        ChangeFollowStateResponse response = null;
        try {
            outcome = table.deleteItem(spec); //If we don't throw an error, delete worked
            response = new ChangeFollowStateResponse(true, null, false);
        }
        catch (Exception e) {
            response = new ChangeFollowStateResponse(false, null, true);
        }
        return response;
    }

    private ChangeFollowStateResponse startFollowing(ChangeFollowStateRequest request){
        ChangeFollowStateResponse response = null;
        try {
            PutItemOutcome outcome = table.putItem(new Item()
                    .withPrimaryKey(PRIMARY_KEY, request.getRootUserAlias(), SORT_KEY, request.getOtherUserAlias()));

            //If we get here, it worked
            response = new ChangeFollowStateResponse(true, null, true);
        }
        catch (Exception e) {
            response = new ChangeFollowStateResponse(false, null, false);
        }
        return response;
    }

    /**
     * returns the state of following between two specified users
     * @param request contains the data of which users to check state between
     * @return the current state of following or not
     */
    public IsFollowingResponse getIsFollowing(IsFollowingRequest request){
        GetItemSpec spec = new GetItemSpec()
                .withPrimaryKey(PRIMARY_KEY, request.getRootUserAlias(),
                        SORT_KEY, request.getOtherUserAlias());
        Item outcome = table.getItem(spec);
        IsFollowingResponse response = null;
        if(outcome != null){  //if the outcome is null, that means the follows does not exist
            response = new IsFollowingResponse(true, null, true);
        } else {
            response = new IsFollowingResponse(true, null, false);
        }
        return response;
    }

    /**
     * Gets the users from the database that the user specified in the request is followed by. Uses
     * information in the request object to limit the number of followers returned and to return the
     * next set of followers after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    public List<String> getFollowers(FollowerRequest request) {
        Index index = table.getIndex("follows_index");

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#fh", "followee_handle");
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":f", request.getFolloweeAlias());
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("#fh = :f")
                .withNameMap(nameMap).withValueMap(valueMap)
                .withMaxResultSize(request.getLimit())
                .withScanIndexForward(true);

        if(request.getLastFollowerAlias() != null){
            spec.withExclusiveStartKey("followee_handle", request.getFolloweeAlias(), "follower_handle", request.getLastFollowerAlias());
        }

        ItemCollection<QueryOutcome> outcome = null;
        List<String> followers = new ArrayList<>();
        try {
            outcome = index.query(spec);
            if (outcome == null){
                return followers;
            }
            Iterator<Item> itemIterator = outcome.iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                followers.add(item.getString("follower_handle"));
            }
        }
        catch (Exception e) {
            System.out.println("Failed query");
            e.printStackTrace();
        }
        return followers;
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
    public List<String> getFollowees(FollowingRequest request) {
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#fh", PRIMARY_KEY);
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":f", request.getFollowerAlias());
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("#fh = :f")
                .withNameMap(nameMap).withValueMap(valueMap)
                .withMaxResultSize(request.getLimit())
                .withScanIndexForward(true);

        if(request.getLastFolloweeAlias() != null){
            spec.withExclusiveStartKey("follower_handle", request.getFollowerAlias(),
                    "followee_handle", request.getLastFolloweeAlias());
        }

        ItemCollection<QueryOutcome> outcome = null;
        List<String> followees = new ArrayList<>();
        try {
            outcome = table.query(spec);
            if(outcome == null){
                return followees;
            }
            Iterator<Item> itemIterator = outcome.iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                followees.add(item.getString("followee_handle"));
            }
        }
        catch (Exception e) {
            System.out.println("Failed query");
            e.printStackTrace();
        }
        return followees;
    }
}
