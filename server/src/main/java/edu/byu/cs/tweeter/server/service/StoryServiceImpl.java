package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.shared.service.StoryService;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class StoryServiceImpl implements StoryService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link StoryDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the story.
     */
    @Override
    public StoryResponse getStory(StoryRequest request) {
        if (!getAuthTokenDAO().checkAuthToken(request.getAuthToken())) {
            return new StoryResponse("AuthToken not found or expired, please logout than back in!");
        }
        return getStatusDAO().getStory(request);
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

    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
