package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

/**
 * The presenter for the "get user" functionality of the application.
 */
public class UserPresenter {

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
    public UserPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the user of the alias specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the user.
     */

    public UserResponse getUser(UserRequest request) throws IOException {
        Service userService = getUserService();
        return (UserResponse) userService.serve((request));
    }

    /**
     * Returns an instance of {@link UserService}. Allows mocking of the UserService class
     * for testing purposes. All usages of FollowingService should get their UserService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getUserService() {
        return new UserService();
    }
}
