package edu.byu.cs.tweeter.model.service.request;

public class IsFollowingRequest extends Request{
    private final String rootUserAlias;
    private final String otherUserAlias;

    /**
     * Creates an instance.
     *
     * @param rootUserAlias the alias of the user who is logged in.
     * @param otherUserAlias the alias of the user to check if following
     */
    public IsFollowingRequest(String rootUserAlias, String otherUserAlias) {
        this.rootUserAlias = rootUserAlias;
        this.otherUserAlias = otherUserAlias;
    }

    /**
     * Returns the alias of the logged in user.
     *
     * @return the root user alias.
     */
    public String getRootUserAlias() {
        return rootUserAlias;
    }

    /**
     * Returns the user to whom we check if root user is following
     *
     * @return the other user alias.
     */
    public String getOtherUserAlias() {
        return otherUserAlias;
    }
}
