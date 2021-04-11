package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.FollowerService;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class FollowerServiceImpl implements FollowerService {

    /**
     * Returns the users that the user specified in the request is followed by. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link FollowsDAO} to
     * get the followers.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new FollowerResponse("AuthToken not found or expired, please logout than back in!");
        }
        List<String> followers = getFollowerDAO().getFollowers(request);
        UserDAO userDAO = getUserDAO();
        List<User> result = new ArrayList<>();
        for(String follower : followers){
            result.add(userDAO.getUser(follower).getUser());
        }
        boolean hasMore = true;
        if(result.size() == 0){
            hasMore = false;
        }
        FollowerResponse response = new FollowerResponse(result, hasMore);
        return response;
    }

    /**
     * Returns an instance of {@link FollowsDAO}. Allows mocking of the FollowerDAO class
     * for testing purposes. All usages of FollowerDAO should get their FollowerDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowsDAO getFollowerDAO() {
        return new FollowsDAO();
    }

    /**
     * Returns an instance of {@link UserDAO}. Allows mocking of the UserDAO class
     * for testing purposes. All usages of UserDAO should get their UserDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    UserDAO getUserDAO() {
        return new UserDAO();
    }

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
