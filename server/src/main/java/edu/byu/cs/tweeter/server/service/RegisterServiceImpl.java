package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.RegisterService;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterServiceImpl implements RegisterService {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (!getUserDAO().getUser(request.getUsername()).getMessage().contains("not found")) {
            return new RegisterResponse(true, "Username already exists, pick a new one or login.");
        }
        getUserDAO().putUser(request);

        return new RegisterResponse(new User(request.getFirstName(),request.getLastName(),
                request.getUsername(), MALE_IMAGE_URL), getAuthTokenDAO().newAuthToken(request.getUsername()));
    }

    UserDAO getUserDAO() { return new UserDAO(); }
    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
