package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class AuthTokenDAO {

    private static final String TableName = "user";
    private static final String UsernameAttr = "alias";
    private static final String FirstAttr = "first_name";
    private static final String LastAttr = "last_name";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public LoginResponse login(LoginRequest request) {
        // search the user table for the user in the request
        Table table = dynamoDB.getTable(TableName);
        Item item = table.getItem(UsernameAttr, request.getUsername());
        if(item != null) // if the user is found in the user table, return it.
            return new LoginResponse(new User(item.getString(FirstAttr), item.getString(LastAttr), item.getString(UsernameAttr), "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken(request.getUsername(),"dummyToken"));
        else
            return new LoginResponse(new User("Not", "Found", "NotAPerson", "https://image.pngaaa.com/171/85171-middle.png"), new AuthToken(request.getUsername(),"dummyToken"));
    }

    public LogoutResponse logout(LogoutRequest request) {
        // invalidate auth token
        return new LogoutResponse(true, "Good job, you logged out");
    }
}
