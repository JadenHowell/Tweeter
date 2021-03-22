package edu.byu.cs.tweeter.shared.service.request;

public class FollowerCountRequest extends Request{
    private String followeeAlias;

    public FollowerCountRequest(String followeeAlias){
        this.followeeAlias = followeeAlias;
    }

    private FollowerCountRequest(){}

    public String getFolloweeAlias(){return followeeAlias;}

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
