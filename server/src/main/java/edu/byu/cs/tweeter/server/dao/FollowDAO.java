package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class FollowDAO {

    /**
     * changes the follow state between two specified users
     * @param request contains the users of whom to change the state of
     * @return the new follow state
     */
    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request){
        //TODO: replace with actual implementation
        ChangeFollowStateResponse response = new ChangeFollowStateResponse(true, null, getDummyState());
        return response;
    }

    /**
     * returns the state of following between two specified users
     * @param request contains the data of which users to check state between
     * @return the current state of following or not
     */
    public IsFollowingResponse getIsFollowing(IsFollowingRequest request){
        //TODO: replace with actual implementation
        IsFollowingResponse response = new IsFollowingResponse(true, null, getDummyState());
        return response;
    }

    /**
     * exists for the purpose of mocking tests
     * @return randomly returns true or false
     */
    public boolean getDummyState(){
        return Math.random() > .5;
    }
}
