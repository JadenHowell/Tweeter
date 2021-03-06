package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.Service;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

/**
 * The presenter for the "post" functionality of the application.
 */
public class PostPresenter {

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
    public PostPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the success message from the post request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the message.
     */
    public PostResponse post(PostRequest request) throws IOException {
        Service postService = getPostService();
        return (PostResponse) postService.serve(request);
    }

    /**
     * Returns an instance of {@link PostService}. Allows mocking of the PostService class
     * for testing purposes. All usages of PostService should get their PostService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    Service getPostService() {
        return new PostService();
    }
}
