package edu.byu.cs.tweeter.server.lambda;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.shared.domain.Follow;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;

public class QueueRetrievalHandler implements RequestHandler<SQSEvent, Void> {

    public static String POSTER_ATTR = "poster";
    public static String TIMESTAMP_ATTR = "timestamp";
    private static final String feedsQueueURL = "https://sqs.us-west-2.amazonaws.com/349051798382/updateFeedsQueue";
    private AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String message = msg.getBody();
            Map<String, SQSEvent.MessageAttribute> attributeMap = msg.getMessageAttributes();
            String alias = attributeMap.get(POSTER_ATTR).getStringValue();
            String timestamp = attributeMap.get(TIMESTAMP_ATTR).getStringValue();

            FollowsDAO followsDAO = getFollowsDAO();
            List<String> f = followsDAO.getFollowers(new FollowerRequest(alias, 10000, null, null));
            LinkedList<String> followers = new LinkedList<>(f);
            for(int i = 0; i<400; i ++) {
                if(followers.size() == 0){
                    break;
                }
                List<String> batchOfFollowers = get25(followers);
                Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
                messageAttributes.put(UpdateFeedsHandler.POSTER_ATTR, new MessageAttributeValue()
                        .withDataType("String")
                        .withStringValue(alias));
                messageAttributes.put(UpdateFeedsHandler.TIMESTAMP_ATTR, new MessageAttributeValue()
                        .withDataType("Number")
                        .withStringValue(String.valueOf(timestamp)));
                messageAttributes.put(UpdateFeedsHandler.FOLLOWERS_ATTR, new MessageAttributeValue()
                        .withDataType("String")
                        .withStringValue(new Gson().toJson(batchOfFollowers)));

                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(feedsQueueURL)
                        .withMessageBody(message)
                        .withMessageAttributes(messageAttributes);
                sqs.sendMessage(send_msg_request);
            }
        }
        return null;
    }

    private static List<String> get25(List<String> original){
        List<String> toReturn = new LinkedList<>();
        for(int i = 0; i < 25; i ++){
            if(original.size()==0){
                break;
            }
            toReturn.add(original.remove(0));
        }
        return toReturn;
    }

    private FollowsDAO getFollowsDAO(){
        return new FollowsDAO();
    }
}
