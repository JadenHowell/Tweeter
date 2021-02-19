package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowerCountService;
import edu.byu.cs.tweeter.model.service.FollowingCountService;
import edu.byu.cs.tweeter.model.service.Service;
import edu.byu.cs.tweeter.model.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingCountResponse;

public class CountPresenter {
    private final CountPresenter.View view;

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
    public CountPresenter(CountPresenter.View view) {
        this.view = view;
    }

    /**
     * Returns the number of users that the user specified in the request is followed by.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followerCount.
     */
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) throws IOException {
        Service followerCountService = getFollowerCountService();
        return (FollowerCountResponse) followerCountService.serve(request);
    }

    /**
     * Returns the number of users that the user specified in the request is following.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followingCount.
     */
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) throws IOException {
        Service followingCountService = getFollowingCountService();
        return (FollowingCountResponse) followingCountService.serve(request);
    }

    /**
     * Returns an instance of {@link FollowingCountService}. Allows mocking of the FollowingCountService class
     * for testing purposes. All usages of FollowingCountService should get their FollowingCountService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getFollowingCountService() {
        return new FollowingCountService();
    }

    /**
     * Returns an instance of {@link FollowerCountService}. Allows mocking of the FollowerCountService class
     * for testing purposes. All usages of FollowerCountService should get their FollowerCountService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getFollowerCountService() {
        return new FollowerCountService();
    }
}
