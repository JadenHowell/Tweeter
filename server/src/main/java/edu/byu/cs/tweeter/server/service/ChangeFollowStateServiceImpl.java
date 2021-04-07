package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.shared.service.ChangeFollowStateService;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;

public class ChangeFollowStateServiceImpl implements ChangeFollowStateService {
    @Override
    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request){
        FollowsDAO followDAO = getFollowsDAO();
        return followDAO.changeFollowState(request);
    }

    public FollowsDAO getFollowsDAO(){return new FollowsDAO();}
}
