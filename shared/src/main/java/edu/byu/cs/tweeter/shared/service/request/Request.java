package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.AuthToken;

public class Request {
    private AuthToken authToken;

    public Request(){}

    public Request(AuthToken authToken){
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
