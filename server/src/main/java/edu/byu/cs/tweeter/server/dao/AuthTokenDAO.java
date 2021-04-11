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

    private static final String TableName = "authtoken";
    private static final String HandleAttr = "alias";
    private static final String AuthToken = "auth_token";
    private static final String TimestampAttr = "timestamp";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public AuthToken newAuthToken(String userAlias) {
        return new AuthToken(userAlias, "dummyToken");
    }

    public LogoutResponse logout(LogoutRequest request) {
        // invalidate auth token
        return new LogoutResponse(true, "Good job, you logged out");
    }
}
