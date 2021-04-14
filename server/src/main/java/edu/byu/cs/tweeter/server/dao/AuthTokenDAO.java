package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.util.Calendar;
import java.util.HashMap;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class AuthTokenDAO {

    private static final String TableName = "authtoken";
    private static final String HandleAttr = "handle";
    private static final String TokenAttr = "auth_token";
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
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
                .build();
        String newToken =  generator.generate(24);
        Table table = dynamoDB.getTable(TableName);
        Item item = new Item()
                .withPrimaryKey(HandleAttr, userAlias)
                .withString(TokenAttr, newToken)
                .withString(TimestampAttr, Long.toString(Calendar.getInstance().getTime().getTime()));
        table.putItem(item);
        return new AuthToken(userAlias, newToken);
    }

    public boolean checkAuthToken(AuthToken authToken) {
        Table table = dynamoDB.getTable(TableName);
        GetItemSpec spec = new GetItemSpec()
                .withPrimaryKey(HandleAttr, authToken.getUserAlias());
        Item outcome = table.getItem(spec);
        if(outcome != null){ // there is no auth token yet
            if (authToken.getToken().equals(outcome.getString(TokenAttr))) {
                Long timestamp = Long.valueOf(outcome.getString(TimestampAttr));
                timestamp += 600000; // 10 minute timeout
                HashMap nameMap = new HashMap<String, String>();
                nameMap.put("#ts", TimestampAttr);
                if (timestamp > Calendar.getInstance().getTime().getTime()){
                    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                            .withPrimaryKey(HandleAttr, authToken.getUserAlias())
                            .withUpdateExpression("set #ts = :t")
                            .withValueMap(new ValueMap().withString(":t", String.valueOf(Calendar.getInstance().getTime().getTime())))
                            .withNameMap(nameMap);
                    table.updateItem(updateItemSpec); // every time an auth token hasn't expired, restart the timeout
                    return true;
                }
            }
        }
        return false;
    }

    public LogoutResponse logout(LogoutRequest request) {
        // invalidate auth token
        Table table = dynamoDB.getTable(TableName);
        try {
            table.deleteItem(new DeleteItemSpec().withPrimaryKey(new PrimaryKey(HandleAttr, request.getUserAlias())));
            return new LogoutResponse(true, "Good job, you logged out");
        }
        catch (Exception e) {
            return new LogoutResponse(true, "error while logging out: " + e);
        }
    }
}
