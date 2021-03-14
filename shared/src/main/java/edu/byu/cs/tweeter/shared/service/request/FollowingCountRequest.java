package edu.byu.cs.tweeter.shared.service.request;

public class FollowingCountRequest extends Request{
    private String followerAlias;

    public FollowingCountRequest(String followerAlias){
        this.followerAlias = followerAlias;
    }

    private FollowingCountRequest(){}

    public String getFollowerAlias(){return followerAlias;}

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }
}
