package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.LogoutDAO;
import edu.byu.cs.tweeter.shared.service.LogoutService;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class LogoutServiceImpl implements LogoutService {
    @Override
    public LogoutResponse logout(LogoutRequest request) { return logMeOutDAO().logout(request); }

    LogoutDAO logMeOutDAO() { return new LogoutDAO(); }
}
