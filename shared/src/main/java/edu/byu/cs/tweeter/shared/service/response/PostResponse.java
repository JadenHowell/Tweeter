package edu.byu.cs.tweeter.shared.service.response;

/**
 * A response for a {@link edu.byu.cs.tweeter.shared.service.request.PostRequest}.
 */
public class PostResponse extends Response {

    public PostResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostResponse(boolean success, String message) {
        super(success, message);
    }
}
