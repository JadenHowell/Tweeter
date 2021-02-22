package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * A response for a {@link edu.byu.cs.tweeter.model.service.request.PostRequest}.
 */
public class PostResponse extends Response {

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostResponse(boolean success, String message) {
        super(success, message);
    }
}
