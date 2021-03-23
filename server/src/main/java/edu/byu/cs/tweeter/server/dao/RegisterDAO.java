package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterDAO {
    public RegisterResponse register(RegisterRequest request) {
        return new RegisterResponse(new User("first_name","last_name","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"), new AuthToken("@TestUser", "nonsenseToken"));
    }
}
