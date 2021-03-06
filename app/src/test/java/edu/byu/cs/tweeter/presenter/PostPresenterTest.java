package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Calendar;

import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.shared.service.request.PostRequest;
import edu.byu.cs.tweeter.shared.service.response.PostResponse;

public class PostPresenterTest {

    private PostRequest request;
    private PostResponse response;
    private PostService mockPostService;
    private PostPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        Status resultStatus1 = new Status(new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"),
                Calendar.getInstance().getTime(), "Status1");

        request = new PostRequest(resultStatus1);
        response = new PostResponse(true, "Post Successful");

        // Create a mock PostService
        mockPostService = Mockito.mock(PostService.class);
        Mockito.when(mockPostService.serve(request)).thenReturn(response);

        // Wrap a PostPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new PostPresenter(new PostPresenter.View() {}));
        Mockito.when(presenter.getPostService()).thenReturn(mockPostService);
    }

    @Test
    public void testPost_returnsServiceResult() throws IOException {
        Mockito.when(mockPostService.serve(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.post(request));
    }

    @Test
    public void testPost_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockPostService.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.post(request);
        });
    }
}
