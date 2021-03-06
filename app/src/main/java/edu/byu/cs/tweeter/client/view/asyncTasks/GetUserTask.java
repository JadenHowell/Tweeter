package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;

public class GetUserTask extends AsyncTask<UserRequest, Void, UserResponse> {

    private final UserPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void getUserSuccessful(UserResponse userResponse);
        void getUserUnsuccessful(UserResponse userResponse);
        void handleException(Exception ex);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter this task should use to get a user.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetUserTask(UserPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on a background thread to log the user in. This method is
     * invoked indirectly by calling {@link #execute(UserRequest...)}.
     *
     * @param userRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected UserResponse doInBackground(UserRequest... userRequests) {
        UserResponse userResponse = null;

        try {
            userResponse = presenter.getUser(userRequests[0]);

            if(userResponse.isSuccess()) {
            }
        } catch (IOException ex) {
            exception = ex;
        }

        return userResponse;
    }

    /**
     * Notifies the observer (on the thread of the invoker of the
     * {@link #execute(UserRequest...)} method) when the task completes.
     *
     * @param userResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(UserResponse userResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else if(userResponse.isSuccess()) {
            observer.getUserSuccessful(userResponse);
        } else {
            observer.getUserUnsuccessful(userResponse);
        }
    }
}
