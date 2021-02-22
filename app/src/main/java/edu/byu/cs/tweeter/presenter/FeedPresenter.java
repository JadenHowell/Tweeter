package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.Service;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

/**
 * The presenter for the "feed" functionality of the application.
 */
public class FeedPresenter {

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
    public FeedPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the statuses that the user specified in the request has in their feed. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the feed.
     */
    public FeedResponse getFeed(FeedRequest request) throws IOException {
        Service feedService = getFeedService();
        return (FeedResponse) feedService.serve(request);
    }

    /**
     * Returns an instance of {@link FeedService}. Allows mocking of the FeedService class
     * for testing purposes. All usages of FeedService should get their FeedService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getFeedService() {
        return new FeedService();
    }
}
