package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowingServiceProxy;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowingResponse;

public class FollowingPresenterTest {

    private FollowingRequest request;
    private FollowingResponse response;
    private FollowingServiceProxy mockFollowingServiceProxy;
    private FollowingPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        request = new FollowingRequest(currentUser.getAlias(), 3, null, new AuthToken("@TestUser", "nonsenseToken"));
        response = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);

        // Create a mock FollowingService
        mockFollowingServiceProxy = Mockito.mock(FollowingServiceProxy.class);
        Mockito.when(mockFollowingServiceProxy.serve(request)).thenReturn(response);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new FollowingPresenter(new FollowingPresenter.View() {}));
        Mockito.when(presenter.getFollowingService()).thenReturn(mockFollowingServiceProxy);
    }

    @Test
    public void testGetFollowing_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowingServiceProxy.serve(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getFollowing(request));
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowingServiceProxy.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollowing(request);
        });
    }
}
