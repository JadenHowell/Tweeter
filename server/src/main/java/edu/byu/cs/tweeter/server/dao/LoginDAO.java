package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class LoginDAO {
    public LoginResponse login(LoginRequest request) {
        return new LoginResponse(new User("first1", "last1", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken("@TestUser", "nonsenseToken"));
    }
}
