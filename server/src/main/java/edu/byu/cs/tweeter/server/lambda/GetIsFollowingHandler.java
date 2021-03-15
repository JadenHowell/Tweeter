package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.IsFollowingServiceImpl;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class GetIsFollowingHandler implements RequestHandler<IsFollowingRequest, IsFollowingResponse> {
    @Override
    public IsFollowingResponse handleRequest(IsFollowingRequest request, Context context) {
        IsFollowingServiceImpl isFollowingService = new IsFollowingServiceImpl();
        return isFollowingService.getIsFollowing(request);
    }
}
