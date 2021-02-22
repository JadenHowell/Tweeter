package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.Request;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.Response;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class UserService extends Service{

    /**
     * Returns the user object that is associated with the user alias specified in the request.
     * Uses the {@link ServerFacade} to get the user object from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the user.
     */
    @Override
    Response accessFacade(Request request) {
        return serverFacade.getUser((UserRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
        loadImages((UserResponse) response);
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
}
