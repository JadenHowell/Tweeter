package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class LogoutDAO {
    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse(true, "Good job, you logged out");
    }
}
