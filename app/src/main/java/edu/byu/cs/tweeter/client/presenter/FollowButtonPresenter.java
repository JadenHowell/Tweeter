package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.ChangeFollowStateServiceProxy;
import edu.byu.cs.tweeter.client.model.service.IsFollowingServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class FollowButtonPresenter {
    private final FollowButtonPresenter.View view;
    boolean isFollowing = false;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public FollowButtonPresenter(FollowButtonPresenter.View view) {
        this.view = view;
    }

    /**
     * Returns the number of users that the user specified in the request is following.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followingCount.
     */
    public IsFollowingResponse getIsFollowing(IsFollowingRequest request) throws IOException, TweeterRemoteException {
        Service isFollowingService = getIsFollowingService();
        IsFollowingResponse response = (IsFollowingResponse) isFollowingService.serve(request);
        isFollowing = response.getIsFollowing();
        return response;
    }

    public ChangeFollowStateResponse changeFollowState(ChangeFollowStateRequest request) throws IOException, TweeterRemoteException {
        Service changeFollowStateService = getChangeFollowStateService();
        return (ChangeFollowStateResponse) changeFollowStateService.serve(request);
    }

    /**
     * Returns an instance of {@link IsFollowingServiceProxy}. Allows mocking of the IsFollowingService class
     * for testing purposes. All usages of IsFollowingService should get their IsFollowingService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getIsFollowingService() {
        return new IsFollowingServiceProxy();
    }

    /**
     * Returns an instance of {@link ChangeFollowStateServiceProxy}. Allows mocking of the ChangeFollowStateService class
     * for testing purposes. All usages of ChangeFollowStateService should get their ChangeFollowStateService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getChangeFollowStateService() {
        return new ChangeFollowStateServiceProxy();
    }

}
