package edu.byu.cs.tweeter.shared.service.response;

public class IsFollowingResponse extends Response{
    boolean isFollowing;

    public IsFollowingResponse(String message) {
        super(false, message);
    }

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
