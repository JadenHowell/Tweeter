package edu.byu.cs.tweeter.model.service.response;

public class IsFollowingResponse extends Response{
    boolean isFollowing;

    public IsFollowingResponse(boolean success, String message, boolean isFollowing){
        super(success, message);
        this.isFollowing = isFollowing;
    }

    /**
     * A method to get the response status
     * @return the following status
     */
    public boolean getIsFollowing(){return isFollowing;}
}
