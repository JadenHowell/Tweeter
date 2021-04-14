package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

public class StoryDAO {

    private static final String TableName = "story";
    private static final String HandleAttr = "alias";
    private static final String TimestampAttr = "timestamp";
    private static final String MessageAttr = "message";

    // DynamoDB client
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

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
        Table table = dynamoDB.getTable(TableName);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", HandleAttr);
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":a", request.getUserAlias());
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("#a = :a")
                .withNameMap(nameMap).withValueMap(valueMap)
                .withMaxResultSize(request.getLimit())
                .withScanIndexForward(true);

        if(request.getLastStatus() != null){
            spec.withExclusiveStartKey(TimestampAttr, request.getLastStatus().getDate());
        }

        ItemCollection<QueryOutcome> outcome = null;
        List<Status> statuses = new ArrayList<>();
        try {
            outcome = table.query(spec);
            if(outcome == null){
                return new StoryResponse(statuses, false);
            }
            Iterator<Item> itemIterator = outcome.iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                statuses.add(new Status(new UserDAO().getUser(request.getUserAlias()).getUser(),
                        Long.parseLong(item.getString(TimestampAttr)), item.getString(MessageAttr)));
            }
        }
        catch (Exception e) {
            System.out.println("Failed query");
            e.printStackTrace();
        }
        Collections.reverse(statuses);
        return new StoryResponse(statuses, true);
    }

    public PostResponse post(PostRequest request) {
        Table table = dynamoDB.getTable(TableName);
        Item item = new Item()
                .withPrimaryKey(HandleAttr, request.getStatus().getUser().getAlias())
                .withString(TimestampAttr, Long.toString(request.getStatus().getDate()))
                .withString(MessageAttr, request.getStatus().getMessage());
        table.putItem(item);
        return new PostResponse(true, "Post Successful!");
    }
}
