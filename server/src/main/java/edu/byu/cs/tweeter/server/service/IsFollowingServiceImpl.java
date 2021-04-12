package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.shared.service.IsFollowingService;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class IsFollowingServiceImpl implements IsFollowingService {
    @Override
    public IsFollowingResponse getIsFollowing(IsFollowingRequest request) {
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new IsFollowingResponse("AuthToken not found or expired, please logout than back in!");
        }
        return getFollowDAO().getIsFollowing(request);
    }

    FollowsDAO getFollowDAO(){return new FollowsDAO();}

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
