package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.shared.service.LoginService;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest request) {
        return logMeInDAO().login(request);
    }

    AuthTokenDAO logMeInDAO() {
        return new AuthTokenDAO();
    }
}
