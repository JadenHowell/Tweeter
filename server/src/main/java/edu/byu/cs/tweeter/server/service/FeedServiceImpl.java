package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.shared.service.FeedService;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FeedServiceImpl implements FeedService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. Uses the {@link FeedDAO} to
     * get the statuses.
     *
     * @param request contains the data required to fulfill the request.
     * @return the feed.
     */
    @Override
    public FeedResponse getFeed(FeedRequest request) {
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new FeedResponse("AuthToken not found or expired, please logout than back in!");
        }
        return getStatusDAO().getFeed(request);
    }

    /**
     * Returns an instance of {@link FeedDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FeedDAO getStatusDAO() {
        return new FeedDAO();
    }

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
