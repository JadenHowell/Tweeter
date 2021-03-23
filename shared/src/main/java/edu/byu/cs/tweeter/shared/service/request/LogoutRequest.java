package edu.byu.cs.tweeter.shared.service.request;


import edu.byu.cs.tweeter.shared.domain.AuthToken;

public class LogoutRequest extends Request{
    private String userAlias;
    private LogoutRequest() {}
    public LogoutRequest(String userAlias, AuthToken authToken){
        super(authToken);
        this.userAlias = userAlias;
    }

    public String getUserAlias() {
        return userAlias;
    }
    public void setUserAlias(String userAlias) { this.userAlias = userAlias; }
}
