package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.shared.service.PostService;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class PostServiceImpl implements PostService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. Uses the {@link StoryDAO} to
     * get the statuses.
     *
     * @param request contains the data required to fulfill the request.
     * @return the feed.
     */
    @Override
    public PostResponse post(PostRequest request) {
        return getStatusDAO().post(request);
    }

    /**
     * Returns an instance of {@link StoryDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StoryDAO getStatusDAO() {
        return new StoryDAO();
    }
}
