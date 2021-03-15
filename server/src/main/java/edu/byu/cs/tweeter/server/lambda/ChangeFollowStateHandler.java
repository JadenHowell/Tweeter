package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.ChangeFollowStateServiceImpl;
import edu.byu.cs.tweeter.shared.service.ChangeFollowStateService;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;

public class ChangeFollowStateHandler implements RequestHandler<ChangeFollowStateRequest, ChangeFollowStateResponse> {

    /**
     * changes the following state between the specified users
     * @param request contains data about who to change follow state of
     * @param context the aws lambda context
     * @return the new follow state of the two users
     */
    @Override
    public ChangeFollowStateResponse handleRequest(ChangeFollowStateRequest request, Context context) {
        ChangeFollowStateServiceImpl changeFollowStateService = new ChangeFollowStateServiceImpl();
        return changeFollowStateService.changeFollowState(request);
    }
}
