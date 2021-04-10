package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.service.FollowingCountService;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class FollowingCountServiceImpl implements FollowingCountService {
    @Override
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
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
}
