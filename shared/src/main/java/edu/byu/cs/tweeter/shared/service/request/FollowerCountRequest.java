package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.AuthToken;

public class FollowerCountRequest extends Request{
    private String followeeAlias;

    public FollowerCountRequest(String followeeAlias, AuthToken authToken){
        super(authToken);
        this.followeeAlias = followeeAlias;
    }

    private FollowerCountRequest(){}

    public String getFolloweeAlias(){return followeeAlias;}

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
