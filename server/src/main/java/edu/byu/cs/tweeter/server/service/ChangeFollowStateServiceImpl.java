package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.service.ChangeFollowStateService;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;

public class ChangeFollowStateServiceImpl implements ChangeFollowStateService {
    @Override
    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request){
        FollowDAO followDAO = getFollowDAO();
        return followDAO.changeFollowState(request);
    }

    public FollowDAO getFollowDAO(){return new FollowDAO();}
}
