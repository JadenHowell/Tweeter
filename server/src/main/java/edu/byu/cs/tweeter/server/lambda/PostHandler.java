package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.PostServiceImpl;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class PostHandler implements RequestHandler<PostRequest, PostResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the post response.
     */
    @Override
    public PostResponse handleRequest(PostRequest request, Context context) {
        PostServiceImpl service = new PostServiceImpl();
        return service.post(request);
    }
}