package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.Status;

/**
 * Contains all the information needed to make a post request.
 */
public class PostRequest extends Request {

    private final Status status;

    /**
     * Creates an instance.
     *
     * @param status to be posted.
     */
    public PostRequest(Status status) {
        this.status = status;
    }

    /**
     * Returns the status to be posted by this request.
     *
     * @return the user.
     */
    public Status getUser() {
        return status;
    }

}
