package edu.byu.cs.tweeter.model.service.request;

public class LogoutRequest extends Request{
    private String userAlias;

    public LogoutRequest(String userAlias){
        this.userAlias = userAlias;
    }

    public String getUserAlias() {
        return userAlias;
    }
}
