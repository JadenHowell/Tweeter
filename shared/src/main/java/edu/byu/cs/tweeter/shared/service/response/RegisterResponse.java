package edu.byu.cs.tweeter.shared.service.response;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;

public class RegisterResponse extends Response {

    private User user;
    private AuthToken authToken;

    public RegisterResponse(String message) {super(false, message);}

    public RegisterResponse(User user, AuthToken authToken) {
        super(true, null);
        this.user = user;
        this.authToken = authToken;
    }

    public User getUser() { return user;}
    public AuthToken getAuthToken() {return authToken;}
    public void setUser(User user) {this.user = user;}
    public void setAuthToken(AuthToken authToken) {this.authToken = authToken;}
}
