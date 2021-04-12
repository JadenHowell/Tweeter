package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import java.util.HashMap;
import java.util.Map;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.lambda.QueueRetrievalHandler;
import edu.byu.cs.tweeter.shared.service.PostService;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class PostServiceImpl implements PostService {

    private String queueURL = "https://sqs.us-west-2.amazonaws.com/349051798382/fetchFollowersQueue";
    private AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. Uses the {@link StoryDAO} to
     * get the statuses.
     *
     * @param request contains the data required to fulfill the request.
     * @return the feed.
     */
    @Override
    public PostResponse post(PostRequest request) {
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new PostResponse(false, "AuthToken not found or expired, please logout than back in!");
        }

        PostResponse response =  getStatusDAO().post(request);

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put(QueueRetrievalHandler.POSTER_ATTR, new MessageAttributeValue()
                .withDataType("String")
                .withStringValue(request.getStatus().getUser().getAlias()));
        messageAttributes.put(QueueRetrievalHandler.TIMESTAMP_ATTR, new MessageAttributeValue()
                .withDataType("Number")
                .withStringValue(String.valueOf(request.getStatus().getDate())));

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueURL)
                .withMessageBody(request.getStatus().getMessage())
                .withMessageAttributes(messageAttributes);
        sqs.sendMessage(send_msg_request);

        return response;
    }

    /**
     * Returns an instance of {@link StoryDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StoryDAO getStatusDAO() {
        return new StoryDAO();
    }

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
