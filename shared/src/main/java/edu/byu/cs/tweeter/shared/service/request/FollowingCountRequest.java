package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.AuthToken;

public class FollowingCountRequest extends Request{
    private String followerAlias;

    public FollowingCountRequest(String followerAlias, AuthToken authToken){
        super(authToken);
        this.followerAlias = followerAlias;
    }

    private FollowingCountRequest(){}

    public String getFollowerAlias(){return followerAlias;}

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }
}
