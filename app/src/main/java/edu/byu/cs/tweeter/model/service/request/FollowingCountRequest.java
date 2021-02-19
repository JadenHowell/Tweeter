package edu.byu.cs.tweeter.model.service.request;

public class FollowingCountRequest extends Request{
    private final String followerAlias;

    public FollowingCountRequest(String followerAlias){
        this.followerAlias = followerAlias;
    }

    public String getFollowerAlias(){return followerAlias;}

}
