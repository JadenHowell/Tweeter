package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Calendar;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

public class PostServiceImplTest {

    private PostRequest request;
    private PostResponse expectedResponse;
    private StoryDAO mockStoryDAO;
    private PostServiceImpl postServiceImplSpy;
    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup() {
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);

        Status resultStatus1 = new Status(new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
                Calendar.getInstance().getTime().getTime(), "Status1");

        // Setup a request object to use in the tests
        request = new PostRequest(resultStatus1, token);

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new PostResponse(true, null);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.post(request)).thenReturn(expectedResponse);

        postServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        Mockito.when(postServiceImplSpy.getStatusDAO()).thenReturn(mockStoryDAO);
        Mockito.when(postServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    /**
     * Verify that the {@link PostServiceImpl#post(PostRequest)}
     * method returns the same result as the {@link StoryDAO} class.
     */
    @Test
    public void testPost_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = postServiceImplSpy.post(request);
        Assertions.assertNull(response.getMessage());
        Assertions.assertTrue(response.isSuccess());
    }
}
