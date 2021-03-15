package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.FollowerCountServiceImpl;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;

public class GetFollowerCountHandler implements RequestHandler<FollowerCountRequest, FollowerCountResponse> {
    /**
     * Returns the number of users that the user specified in the request is followed by.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public FollowerCountResponse handleRequest(FollowerCountRequest request, Context context) {
        FollowerCountServiceImpl service = new FollowerCountServiceImpl();
        return service.getFollowerCount(request);
    }
}
