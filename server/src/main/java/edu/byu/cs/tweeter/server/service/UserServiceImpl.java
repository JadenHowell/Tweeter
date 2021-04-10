package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.service.UserService;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class UserServiceImpl implements UserService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. Uses the {@link UserDAO} to
     * get the statuses.
     *
     * @param request contains the data required to fulfill the request.
     * @return the feed.
     */
    @Override
    public UserResponse getUser(UserRequest request) {
        return getUserDAO().getUser(request.getUserAlias());
    }

    /**
     * Returns an instance of {@link UserDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
