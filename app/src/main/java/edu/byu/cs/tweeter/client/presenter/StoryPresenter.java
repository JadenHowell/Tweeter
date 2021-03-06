package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

/**
 * The presenter for the "story" functionality of the application.
 */
public class StoryPresenter {

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
    public StoryPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the statuses that the user specified in the request has in their story. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the story.
     */
    public StoryResponse getStory(StoryRequest request) throws IOException {
        Service storyService = getStoryService();
        return (StoryResponse) storyService.serve(request);
    }

    /**
     * Returns an instance of {@link StoryService}. Allows mocking of the StoryService class
     * for testing purposes. All usages of FollowingService should get their StoryService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getStoryService() {
        return new StoryService();
    }
}
