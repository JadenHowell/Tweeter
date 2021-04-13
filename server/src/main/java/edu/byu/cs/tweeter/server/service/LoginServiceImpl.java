package edu.byu.cs.tweeter.server.service;

import com.password4j.Password;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.service.LoginService;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest request) {
        if (getUserDAO().getUser(request.getUsername()).getMessage().contains("not found")){
            return new LoginResponse(true, "Username does not exist.");
        }

        String hash = getUserDAO().getHash(request.getUsername());
        String hashfromDB = hash.split("==")[0] + "==";
        String saltfromDB = hash.split("==")[1];
        boolean match = getMatch(request.getPassword(), saltfromDB, hashfromDB);

        if (match) {
            return new LoginResponse(getUserDAO().getUser(request.getUsername()).getUser(),
                    getAuthTokenDAO().newAuthToken(request.getUsername()));
        } else {
            return new LoginResponse(true, "Password is not correct.");
        }
    }

    public boolean getMatch(String password, String saltFromDB, String hashFromDB){
        return Password.check(password, hashFromDB)
                .addSalt(saltFromDB)
                .withPBKDF2();
    }

    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
    UserDAO getUserDAO() { return new UserDAO(); }
}
