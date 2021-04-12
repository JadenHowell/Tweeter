package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.client.presenter.FollowButtonPresenter;

public class ChangeFollowStateTask extends AsyncTask<ChangeFollowStateRequest, Void, ChangeFollowStateResponse> {
    private final FollowButtonPresenter presenter;
    private final ChangeFollowStateTask.ChangeStateObserver observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface ChangeStateObserver{
        void changeButtonStateRetrieved(ChangeFollowStateResponse changeFollowStateResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followers.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public ChangeFollowStateTask(FollowButtonPresenter presenter, ChangeFollowStateTask.ChangeStateObserver observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followers. This method is
     * invoked indirectly by calling {@link #execute(ChangeFollowStateRequest...)}.
     *
     * @param ChangeFollowStateRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected ChangeFollowStateResponse doInBackground(ChangeFollowStateRequest... ChangeFollowStateRequests) {

        ChangeFollowStateResponse response = null;

        try {
            response = presenter.changeFollowState(ChangeFollowStateRequests[0]);
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param changeFollowStateResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(ChangeFollowStateResponse changeFollowStateResponse) {
        if(!changeFollowStateResponse.isSuccess()) {
            exception = new Exception(changeFollowStateResponse.getMessage());
        }
        if(exception != null) {
            observer.handleException(exception);
        } else  {
            observer.changeButtonStateRetrieved(changeFollowStateResponse);
        }
    }
}
