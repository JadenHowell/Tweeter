package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Calendar;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

public class PostPresenterTest {

    private PostRequest request;
    private PostResponse response;
    private PostServiceProxy mockPostServiceProxy;
    private PostPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        Status resultStatus1 = new Status(new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
                Calendar.getInstance().getTime().getTime(), "Status1");

        request = new PostRequest(resultStatus1);
        response = new PostResponse(true, "Post Successful");

        // Create a mock PostService
        mockPostServiceProxy = Mockito.mock(PostServiceProxy.class);
        Mockito.when(mockPostServiceProxy.serve(request)).thenReturn(response);

        // Wrap a PostPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new PostPresenter(new PostPresenter.View() {}));
        Mockito.when(presenter.getPostService()).thenReturn(mockPostServiceProxy);
    }

    @Test
    public void testPost_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockPostServiceProxy.serve(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.post(request));
    }

    @Test
    public void testPost_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockPostServiceProxy.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.post(request);
        });
    }
}
