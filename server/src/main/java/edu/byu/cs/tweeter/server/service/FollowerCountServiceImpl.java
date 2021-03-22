package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.shared.service.FollowerCountService;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;

public class FollowerCountServiceImpl implements FollowerCountService {
    @Override
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request){
        return getFollowerDAO().getFollowerCount(request);
    }

    /**
     * Returns an instance of {@link FollowerDAO}. Allows mocking of the FollowerDAO class
     * for testing purposes. All usages of FollowerDAO should get their FollowerDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowerDAO getFollowerDAO() {
        return new FollowerDAO();
    }
}
