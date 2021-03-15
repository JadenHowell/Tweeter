package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.FollowerService;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowerServiceProxy extends Service implements FollowerService {

    /**
     * Returns the users that the user specified in the request is being followed by. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the followers from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return getFollowers((FollowerRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
        loadImages((FollowerResponse) response);
    }

    /**
     * Loads the profile image data for each follower included in the response.
     *
     * @param response the response from the follower request.
     */
    private void loadImages(FollowerResponse response) throws IOException {
        for(User user : response.getFollowers()) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    @Override
    public FollowerResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.getFollowers(request);
    }
}
