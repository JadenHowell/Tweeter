package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class PostService extends Service{

    /**
     * Returns a message stating if the post was successful or not. Uses the {@link ServerFacade} to
     * post the status to the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the post success.
     */
    @Override
    Response accessFacade(Request request) {
        return serverFacade.post((PostRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }
}
