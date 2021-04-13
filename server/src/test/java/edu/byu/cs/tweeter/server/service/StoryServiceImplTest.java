package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

public class StoryServiceImplTest {

    private StoryRequest request;
    private StoryResponse expectedResponse;
    private StoryDAO mockStoryDAO;
    private StoryServiceImpl storyServiceImplSpy;
    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup() {
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        AuthToken token = new AuthToken("@TestUser", "nonsenseToken");
        Mockito.when(mockAuthTokenDAO.checkAuthToken(token)).thenReturn(true);
        User currentUser = new User("FirstName", "LastName", null);

        Status resultStatus1 = new Status(new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
                Calendar.getInstance().getTime().getTime(), "Status1");
        Status resultStatus2 = new Status(new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png"),
                Calendar.getInstance().getTime().getTime(), "Status2");
        Status resultStatus3 = new Status(new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png"),
                Calendar.getInstance().getTime().getTime(), "Status3");

        // Setup a request object to use in the tests
        request = new StoryRequest(currentUser.getAlias(), 3, null, token);

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new StoryResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.getStory(request)).thenReturn(expectedResponse);

        storyServiceImplSpy = Mockito.spy(StoryServiceImpl.class);
        Mockito.when(storyServiceImplSpy.getStatusDAO()).thenReturn(mockStoryDAO);
        Mockito.when(storyServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    /**
     * Verify that the {@link StoryServiceImpl#getStory(StoryRequest)}
     * method returns the same result as the {@link StoryDAO} class.
     */
    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceImplSpy.getStory(request);
        Assertions.assertEquals(expectedResponse.getStatusList(), response.getStatusList());
    }
}
