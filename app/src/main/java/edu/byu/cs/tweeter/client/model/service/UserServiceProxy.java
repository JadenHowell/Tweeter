package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.UserService;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.Response;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class UserServiceProxy extends Service implements UserService {

    /**
     * Returns the user object that is associated with the user alias specified in the request.
     * Uses the {@link ServerFacade} to get the user object from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the user.
     */
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return getUser((UserRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
        if (!(response.getMessage() == null) && !response.getMessage().contains("not found")) {
            loadImages((UserResponse) response);
        }
    }

    /**
     * Loads the profile image data for the user included in the response.
     *
     * @param response the response from the user request.
     */
    private void loadImages(UserResponse response) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(response.getUser().getImageUrl());
        response.getUser().setImageBytes(bytes);
    }

    @Override
    public UserResponse getUser(UserRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.getUser(request);
    }
}
