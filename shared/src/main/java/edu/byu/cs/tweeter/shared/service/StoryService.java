package edu.byu.cs.tweeter.shared.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

/**
 * Defines the interface for the 'following' service.
 */
public interface StoryService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the story.
     */
    StoryResponse getStory(StoryRequest request)
            throws IOException, TweeterRemoteException;
}