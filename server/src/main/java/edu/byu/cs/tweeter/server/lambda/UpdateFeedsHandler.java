package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void> {
    public static String POSTER_ATTR = "poster";
    public static String TIMESTAMP_ATTR = "timestamp";
    public static String FOLLOWERS_ATTR = "followers";
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String message = msg.getBody();
            Map<String, SQSEvent.MessageAttribute> attributeMap = msg.getMessageAttributes();
            String alias = attributeMap.get(POSTER_ATTR).getStringValue();
            long timestamp = Long.parseLong(attributeMap.get(TIMESTAMP_ATTR).getStringValue());
            String stringFollowers = attributeMap.get(FOLLOWERS_ATTR).getStringValue();
            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            List<String> followers = (new Gson()).fromJson(stringFollowers, listType);
            System.out.println("alias: " + alias + "\nmessage: " + message + "\ntimestamp: " + timestamp + "\nfollowers: "+ followers);

        }
        return null;
    }
}
