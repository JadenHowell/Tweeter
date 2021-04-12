package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.service.FollowerCountService;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;

public class FollowerCountServiceImpl implements FollowerCountService {
    @Override
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request){
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new FollowerCountResponse("AuthToken not found or expired, please logout than back in!");
        }
        return getFollowerDAO().getFollowerCount(request);
    }

    /**
     * Returns an instance of {@link UserDAO}. Allows mocking of the FollowerDAO class
     * for testing purposes. All usages of FollowerDAO should get their FollowerDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    UserDAO getFollowerDAO() {
        return new UserDAO();
    }

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
