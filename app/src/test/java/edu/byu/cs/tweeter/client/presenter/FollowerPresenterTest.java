package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerResponse;

public class FollowerPresenterTest {

    private FollowerRequest request;
    private FollowerResponse response;
    private FollowerServiceProxy mockFollowerServiceProxy;
    private FollowerPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        request = new FollowerRequest(currentUser.getAlias(), 3, null, new AuthToken("@TestUser", "nonsenseToken"));
        response = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);

        // Create a mock FollowingService
        mockFollowerServiceProxy = Mockito.mock(FollowerServiceProxy.class);
        Mockito.when(mockFollowerServiceProxy.serve(request)).thenReturn(response);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new FollowerPresenter(new FollowerPresenter.View() {}));
        Mockito.when(presenter.getFollowerService()).thenReturn(mockFollowerServiceProxy);
    }

    @Test
    public void testGetFollower_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowerServiceProxy.serve(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getFollower(request));
    }

    @Test
    public void testGetFollower_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowerServiceProxy.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollower(request);
        });
    }
}
