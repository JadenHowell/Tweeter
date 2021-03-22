package edu.byu.cs.tweeter.shared.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified story.
 */
public class StoryRequest extends Request {

    private String userAlias;
    private int limit;
    private String lastStatus;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private StoryRequest() {}

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user whose story is to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastStatus the alias of the last status that was returned in the previous request (null if
     *                     there was no previous request or if no statuses were returned in the
     *                     previous request).
     */
    public StoryRequest(String userAlias, int limit, String lastStatus) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    /**
     * Returns the user alias whose story is to be returned by this request.
     *
     * @return the user alias.
     */
    public String getUserAlias() {
        return userAlias;
    }

    /**
     * Returns the number representing the maximum number of statuses to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last status that was returned in the previous request or null if there was no
     * previous request or if no statuses were returned in the previous request.
     *
     * @return the last status.
     */
    public String getLastStatus() {
        return lastStatus;
    }
}
