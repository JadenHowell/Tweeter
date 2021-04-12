package edu.byu.cs.tweeter.shared.service.response;


public class ChangeFollowStateResponse extends Response {
    boolean newFollowingState;

    public ChangeFollowStateResponse(boolean success, String msg, boolean newFollowingState){
        super(success, msg);
        this.newFollowingState = newFollowingState;
    }

    public ChangeFollowStateResponse(boolean success, String message){
        super(success, message);
    }

    public boolean getNewFollowingState(){return newFollowingState; }
}
