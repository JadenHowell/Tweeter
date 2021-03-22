package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.PostService;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.Response;

public class PostServiceProxy extends Service implements PostService {

    /**
     * Returns a message stating if the post was successful or not. Uses the {@link ServerFacade} to
     * post the status to the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the post success.
     */
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return post((PostRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
    }

    @Override
    public Response post(PostRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.post(request);
    }
}
