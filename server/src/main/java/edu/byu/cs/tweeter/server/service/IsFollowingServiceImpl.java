package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.service.IsFollowingService;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class IsFollowingServiceImpl implements IsFollowingService {
    @Override
    public IsFollowingResponse getIsFollowing(IsFollowingRequest request){
        FollowDAO followDAO = getFollowDAO();
        return followDAO.getIsFollowing(request);
    }

    public FollowDAO getFollowDAO(){return new FollowDAO();}
}
