package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.service.response.FeedResponse;

public class FeedServiceImplTest {

    private FeedRequest request;
    private FeedResponse expectedResponse;
    private StatusDAO mMockStatusDAO;
    private FeedServiceImpl feedServiceImplSpy;

    @BeforeEach
    public void setup() {
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
        request = new FeedRequest(currentUser.getAlias(), 3, null);

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new FeedResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
        mMockStatusDAO = Mockito.mock(StatusDAO.class);
        Mockito.when(mMockStatusDAO.getFeed(request)).thenReturn(expectedResponse);

        feedServiceImplSpy = Mockito.spy(FeedServiceImpl.class);
        Mockito.when(feedServiceImplSpy.getStatusDAO()).thenReturn(mMockStatusDAO);
    }

    /**
     * Verify that the {@link FeedServiceImpl#getFeed(FeedRequest)}
     * method returns the same result as the {@link StatusDAO} class.
     */
    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceImplSpy.getFeed(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
