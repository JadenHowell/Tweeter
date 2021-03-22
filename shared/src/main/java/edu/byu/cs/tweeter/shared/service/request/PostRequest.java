package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.Status;

/**
 * Contains all the information needed to make a post request.
 */
public class PostRequest extends Request {

    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostRequest() {}

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
