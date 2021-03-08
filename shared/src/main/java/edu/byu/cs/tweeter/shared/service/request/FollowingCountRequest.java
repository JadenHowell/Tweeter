package edu.byu.cs.tweeter.shared.service.request;

public class FollowingCountRequest extends Request{
    private final String followerAlias;

    public FollowingCountRequest(String followerAlias){
        this.followerAlias = followerAlias;
    }

    public String getFollowerAlias(){return followerAlias;}

}
