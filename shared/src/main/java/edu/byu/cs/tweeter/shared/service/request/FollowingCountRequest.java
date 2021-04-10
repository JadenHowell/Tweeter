package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.AuthToken;

public class FollowingCountRequest extends Request{
    private String userAlias;

    public FollowingCountRequest(String userAlias, AuthToken authToken){
        super(authToken);
        this.userAlias = userAlias;
    }

    private FollowingCountRequest(){}

    public String getUserAlias(){return userAlias;}

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
