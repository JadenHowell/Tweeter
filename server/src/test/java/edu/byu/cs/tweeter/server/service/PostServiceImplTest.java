package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Calendar;

import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

public class PostServiceImplTest {

    private PostRequest request;
    private PostResponse expectedResponse;
    private StatusDAO mockStatusDAO;
    private PostServiceImpl postServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);
        Status resultStatus1 = new Status(new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
                Calendar.getInstance().getTime().getTime(), "Status1");

        // Setup a request object to use in the tests
        request = new PostRequest(resultStatus1);

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new PostResponse(true, null);
        mockStatusDAO = Mockito.mock(StatusDAO.class);
        Mockito.when(mockStatusDAO.post(request)).thenReturn(expectedResponse);

        postServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        Mockito.when(postServiceImplSpy.getStatusDAO()).thenReturn(mockStatusDAO);
    }

    /**
     * Verify that the {@link PostServiceImpl#post(PostRequest)}
     * method returns the same result as the {@link StatusDAO} class.
     */
    @Test
    public void testPost_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = postServiceImplSpy.post(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
