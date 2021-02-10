package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowerService;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;

/**
 * The presenter for the "follower" functionality of the application.
 */
public class FollowerPresenter {
    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public FollowerPresenter(FollowerPresenter.View view) {
        this.view = view;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    public FollowerResponse getFollower(FollowerRequest request) throws IOException {
        FollowerService followerService = getFollowerService();
        return (FollowerResponse) followerService.serve(request);
    }

    /**
     * Returns an instance of {@link FollowerService}. Allows mocking of the FollowerService class
     * for testing purposes. All usages of FollowerService should get their FollowerService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowerService getFollowerService() {
        return new FollowerService();
    }
}
