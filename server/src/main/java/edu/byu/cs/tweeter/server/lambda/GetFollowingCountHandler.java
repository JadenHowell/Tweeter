package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.FollowingCountServiceImpl;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class GetFollowingCountHandler implements RequestHandler<FollowingCountRequest, FollowingCountResponse> {
    /**
     * Returns the number of users that the user specified in the request is following.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public FollowingCountResponse handleRequest(FollowingCountRequest request, Context context) {
        FollowingCountServiceImpl service = new FollowingCountServiceImpl();
        return service.getFollowingCount(request);
    }
}
