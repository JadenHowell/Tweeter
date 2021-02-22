package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.presenter.CountPresenter;

public class GetFollowerCountTask extends AsyncTask<FollowerCountRequest, Void, FollowerCountResponse> {

    private final CountPresenter presenter;
    private final GetFollowerCountTask.FollowerCountObserver observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface FollowerCountObserver{
        void followerCountRetrieved(FollowerCountResponse followerCountResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followers.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowerCountTask(CountPresenter presenter, GetFollowerCountTask.FollowerCountObserver observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followers. This method is
     * invoked indirectly by calling {@link #execute(FollowerCountRequest...)}.
     *
     * @param followerCountRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowerCountResponse doInBackground(FollowerCountRequest... followerCountRequests) {

        FollowerCountResponse response = null;

        try {
            response = presenter.getFollowerCount(followerCountRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followerCountResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowerCountResponse followerCountResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.followerCountRetrieved(followerCountResponse);
        }
    }
}
