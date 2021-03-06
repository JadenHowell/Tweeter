package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Calendar;

import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

public class PostServiceTest {

    private PostRequest validRequest;
    private PostRequest invalidRequest;

    private PostResponse successResponse;
    private PostResponse failureResponse;

    private PostService postServiceSpy;

    /**
     * Create a PostService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        Status resultStatus1 = new Status(new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
                Calendar.getInstance().getTime(), "Status1");

        // Setup request objects to use in the tests
        validRequest = new PostRequest(resultStatus1);
        invalidRequest = new PostRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostResponse(true, null);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.post(validRequest)).thenReturn(successResponse);

        failureResponse = new PostResponse(false, "An exception occurred");
        Mockito.when(mockServerFacade.post(invalidRequest)).thenReturn(failureResponse);

        // Create a PostService instance and wrap it with a spy that will use the mock service
        postServiceSpy = Mockito.spy(new PostService());
        Mockito.when(postServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the PostService.serve(PostRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testPost_validRequest_correctResponse() throws IOException {
        PostResponse response = (PostResponse) postServiceSpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the PostService.serve(PostRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testPost_invalidRequest_returnsInvalidResponse() throws IOException {
        PostResponse response = (PostResponse) postServiceSpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
