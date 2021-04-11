package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.service.FollowingCountService;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class FollowingCountServiceImpl implements FollowingCountService {
    @Override
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new FollowingCountResponse("AuthToken not found or expired, please logout than back in!");
        }
        return getFollowingDAO().getFolloweeCount(request);
    }

    /**
     * Returns an instance of {@link UserDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    UserDAO getFollowingDAO() {
        return new UserDAO();
    }

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
