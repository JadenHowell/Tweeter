package edu.byu.cs.tweeter.model.service.request;

public class FollowerCountRequest extends Request{
    private final String followeeAlias;

    public FollowerCountRequest(String followeeAlias){
        this.followeeAlias = followeeAlias;
    }

    public String getFolloweeAlias(){return followeeAlias;}

}