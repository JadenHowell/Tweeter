package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LogoutResponse extends Response {

    private User user;
    private AuthToken authToken;

    public LogoutResponse(String message) {super(false, message);}

    public LogoutResponse(User user, AuthToken authToken) {
        super(true, null);
        this.user = user;
        this.authToken = authToken;
    }

    public User getUser() {return user;}
    public AuthToken getAuthToken() {return authToken;}
}
