package edu.byu.cs.tweeter.model.service.response;


public class ChangeFollowStateResponse extends Response{
    boolean newFollowingState;

    public ChangeFollowStateResponse(boolean success, String msg, boolean newFollowingState){
        super(success, msg);
        this.newFollowingState = newFollowingState;
    }

    public boolean getNewFollowingState(){return newFollowingState; }
}