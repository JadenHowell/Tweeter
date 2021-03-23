package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.StoryServiceImpl;
import edu.byu.cs.tweeter.server.service.UserServiceImpl;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetUserHandler implements RequestHandler<UserRequest, UserResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the story.
     */
    @Override
    public UserResponse handleRequest(UserRequest request, Context context) {
        UserServiceImpl service = new UserServiceImpl();
        return service.getUser(request);
    }
}