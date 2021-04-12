package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;
import edu.byu.cs.tweeter.client.presenter.CountPresenter;

public class GetFollowingCountTask extends AsyncTask<FollowingCountRequest, Void, FollowingCountResponse> {

    private final CountPresenter presenter;
    private final GetFollowingCountTask.FollowingCountObserver observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface FollowingCountObserver{
        void followingCountRetrieved(FollowingCountResponse followingCountResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followers.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowingCountTask(CountPresenter presenter, GetFollowingCountTask.FollowingCountObserver observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followers. This method is
     * invoked indirectly by calling {@link #execute(FollowingCountRequest...)}.
     *
     * @param followingCountRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowingCountResponse doInBackground(FollowingCountRequest... followingCountRequests) {

        FollowingCountResponse response = null;

        try {
            response = presenter.getFollowingCount(followingCountRequests[0]);
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followingCountResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowingCountResponse followingCountResponse) {
        if(!followingCountResponse.isSuccess()) {
            exception = new Exception(followingCountResponse.getMessage());
        }
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.followingCountRetrieved(followingCountResponse);
        }
    }
}

