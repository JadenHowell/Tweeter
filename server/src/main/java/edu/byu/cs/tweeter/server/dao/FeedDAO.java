package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import org.graalvm.compiler.lir.gen.ArithmeticLIRGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

public class FeedDAO {
    private static final String TABLE_NAME = "feed";
    private static final String POSTER_ALIAS = "poster-alias";
    private static final String USER_ALIAS = "alias";
    private static final String TIMESTAMP_ATTR = "timestamp";
    private static final String MESSAGE_ATTR = "message";

    // DynamoDB client
    private static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
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
        Table table = dynamoDB.getTable(TABLE_NAME);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", USER_ALIAS);
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":a", request.getUserAlias());
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("#a = :a")
                .withNameMap(nameMap).withValueMap(valueMap)
                .withMaxResultSize(request.getLimit())
                .withScanIndexForward(false);

        if(request.getLastStatus() != null){
            spec.withExclusiveStartKey(TIMESTAMP_ATTR, request.getLastStatus().getDate());
        }

        ItemCollection<QueryOutcome> outcome = null;
        List<Status> statuses = new ArrayList<>();
        try {
            outcome = table.query(spec);
            if(outcome == null){
                return new FeedResponse(statuses, false);
            }
            Iterator<Item> itemIterator = outcome.iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                statuses.add(new Status(new UserDAO().getUser(item.getString(POSTER_ALIAS)).getUser(),
                        Long.parseLong(item.getString(TIMESTAMP_ATTR)), item.getString(MESSAGE_ATTR)));
            }
        }
        catch (Exception e) {
            System.out.println("Failed query");
            e.printStackTrace();
        }
        return new FeedResponse(statuses, true);
    }

    public void postToFeed(List<String> followers, String posterAlias, String message, String timestamp) {
        TableWriteItems writeItems = new TableWriteItems(TABLE_NAME);
        for(int i = 0; i < followers.size(); i ++) {
            Item item = new Item()
                    .withPrimaryKey(USER_ALIAS, followers.get(i))
                    .withString(TIMESTAMP_ATTR, timestamp)
                    .withString(MESSAGE_ATTR, message)
                    .withString(POSTER_ALIAS, posterAlias);

            writeItems.addItemToPut(item);
        }
        System.out.println("doing batch write with message " + message);
        dynamoDB.batchWriteItem(writeItems);
    }
}
