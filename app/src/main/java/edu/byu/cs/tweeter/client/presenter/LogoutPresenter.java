package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class LogoutPresenter {

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
    public LogoutPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the logout success
     *
     * @return the followees.
     */
    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        Service logoutService = getLogoutService();
        return (LogoutResponse) logoutService.serve(request);
    }

    Service getLogoutService() {
        return new LogoutServiceProxy();
    }
}
