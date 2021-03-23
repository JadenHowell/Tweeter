package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followers for a specified follower.
 */
public class FollowerRequest extends Request {
    private String followeeAlias;
    private int limit;
    private String lastFollowerAlias;

    /**
     * Creates an instance.
     *
     * @param followeeAlias the alias of the user whose followers are to be returned.
     * @param limit the maximum number of followers to return.
     * @param lastFollowerAlias the alias of the last follower that was returned in the previous request (null if
     *                     there was no previous request or if no followers were returned in the
     *                     previous request).
     */
    public FollowerRequest(String followeeAlias, int limit, String lastFollowerAlias, AuthToken authToken){
        super(authToken);
        this.followeeAlias = followeeAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }

    private FollowerRequest(){}

    /**
     * Returns the followee whose followers are to be returned by this request.
     *
     * @return the follower.
     */
    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {this.followeeAlias = followeeAlias;}

    /**
     * Returns the number representing the maximum number of followers to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {this.limit = limit;}

    /**
     * Returns the last follower that was returned in the previous request or null if there was no
     * previous request or if no followers were returned in the previous request.
     *
     * @return the last follower.
     */
    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    public void setLastFollowerAlias(String lastFollowerAlias) {this.lastFollowerAlias = lastFollowerAlias;}

}
