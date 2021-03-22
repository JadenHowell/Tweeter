package edu.byu.cs.tweeter.shared.service.request;

public class IsFollowingRequest extends Request{
    private String rootUserAlias;
    private String otherUserAlias;

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

    private IsFollowingRequest(){}

    /**
     * Returns the alias of the logged in user.
     *
     * @return the root user alias.
     */
    public String getRootUserAlias() {
        return rootUserAlias;
    }
    public void setRootUserAlias(String rootUserAlias) {this.rootUserAlias = rootUserAlias;}

    /**
     * Returns the user to whom we check if root user is following
     *
     * @return the other user alias.
     */
    public String getOtherUserAlias() {
        return otherUserAlias;
    }

    public void setOtherUserAlias(String otherUserAlias) {
        this.otherUserAlias = otherUserAlias;
    }
}
