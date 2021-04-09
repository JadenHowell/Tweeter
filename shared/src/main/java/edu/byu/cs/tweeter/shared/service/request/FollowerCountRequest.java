package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.AuthToken;

public class FollowerCountRequest extends Request{
    private String userAlias;

    public FollowerCountRequest(String userAlias, AuthToken authToken){
        super(authToken);
        this.userAlias = userAlias;
    }

    private FollowerCountRequest(){}

    public String getUserAlias(){return userAlias;}

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
