package edu.byu.cs.tweeter.shared.service.response;


public class LogoutResponse extends Response{

    public LogoutResponse(boolean success, String message) {
        super(success, message);
    }
}
