package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.FeedService;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.service.response.Response;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
 * Contains the business logic for getting the statuses in the feed of a user.
 */
public class FeedServiceProxy extends Service implements FeedService {

    /**
     * Returns the statuses that the user specified in the request has in their feed. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the statuses from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses.
     */
    @Override
    Response accessFacade(Request request) throws IOException, TweeterRemoteException {
        return getFeed((FeedRequest) request);
    }

    @Override
    void onSuccess(Response response) throws IOException {
        loadImages((FeedResponse) response);
    }

    /**
     * Loads the profile image data for each status included in the response.
     *
     * @param response the response from the feed request.
     */
    private void loadImages(FeedResponse response) throws IOException {
        for(Status status : response.getStatusList()) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(status.getUser().getImageUrl());
            status.getUser().setImageBytes(bytes);
        }
    }

    @Override
    public FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        return serverFacade.getFeed(request);
    }
}
