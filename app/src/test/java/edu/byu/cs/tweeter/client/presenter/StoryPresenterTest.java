package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.service.StoryService;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.service.response.StoryResponse;

public class StoryPresenterTest {

    private StoryRequest request;
    private StoryResponse response;
    private StoryServiceProxy mockStoryServiceProxy;
    private StoryPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
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

        request = new StoryRequest(currentUser.getAlias(), 3, null, new AuthToken("@TestUser", "nonsenseToken"));
        response = new StoryResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);

        // Create a mock StoryService
        mockStoryServiceProxy = Mockito.mock(StoryServiceProxy.class);
        Mockito.when(mockStoryServiceProxy.serve(request)).thenReturn(response);

        // Wrap a StoryPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new StoryPresenter(new StoryPresenter.View() {}));
        Mockito.when(presenter.getStoryService()).thenReturn(mockStoryServiceProxy);
    }

    @Test
    public void testGetStory_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryServiceProxy.serve(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getStory(request));
    }

    @Test
    public void testGetStory_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryServiceProxy.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStory(request);
        });
    }
}
