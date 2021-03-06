package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.client.presenter.FollowButtonPresenter;

public class GetIsFollowingTask extends AsyncTask<IsFollowingRequest, Void, IsFollowingResponse> {
    private final FollowButtonPresenter presenter;
    private final GetIsFollowingTask.IsFollowingObserver observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface IsFollowingObserver{
        void isFollowingRetrieved(IsFollowingResponse isFollowingResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followers.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetIsFollowingTask(FollowButtonPresenter presenter, GetIsFollowingTask.IsFollowingObserver observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followers. This method is
     * invoked indirectly by calling {@link #execute(IsFollowingRequest...)}.
     *
     * @param isFollowingRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected IsFollowingResponse doInBackground(IsFollowingRequest... isFollowingRequests) {

        IsFollowingResponse response = null;

        try {
            response = presenter.getIsFollowing(isFollowingRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param isFollowingResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(IsFollowingResponse isFollowingResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.isFollowingRetrieved(isFollowingResponse);
        }
    }
}
