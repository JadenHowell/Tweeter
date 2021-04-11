package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.service.ChangeFollowStateService;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class ChangeFollowStateServiceImpl implements ChangeFollowStateService {
    @Override
    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request){
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new ChangeFollowStateResponse(false, "AuthToken not found or expired, please logout than back in!");
        }
        FollowsDAO followDAO = getFollowsDAO();
        ChangeFollowStateResponse response = followDAO.changeFollowState(request);
        getUserDAO().updateFollowCounts(request, response);
        return response;
    }

    public FollowsDAO getFollowsDAO(){return new FollowsDAO();}

    public UserDAO getUserDAO(){return new UserDAO();}

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
