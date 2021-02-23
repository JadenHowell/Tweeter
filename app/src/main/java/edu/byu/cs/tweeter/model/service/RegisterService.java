package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.Service;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.Request;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.Response;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class RegisterService extends Service {

    @Override
    Response accessFacade(Request request) {return serverFacade.register((RegisterRequest) request);}

    @Override
    void onSuccess(Response response) throws IOException {
        loadImage(((RegisterResponse) response).getUser());
    }

    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }
}