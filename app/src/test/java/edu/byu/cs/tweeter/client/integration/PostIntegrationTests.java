package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

public class PostIntegrationTests {

    private Service postService;

    @Test
    public void postIsSuccess() throws IOException, TweeterRemoteException {
        Status resultStatus1 = new Status(new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
                Calendar.getInstance().getTime().getTime(), "Status1");

        postService = new PostServiceProxy();
        PostResponse expectedResponse = new PostResponse(true, "Post Successful!");
        PostRequest request = new PostRequest(resultStatus1, new AuthToken("@TestUser", "nonsenseToken"));
        PostResponse response = (PostResponse) postService.serve(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
}
