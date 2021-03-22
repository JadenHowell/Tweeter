package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class UserPresenterTest {

    private UserRequest request;
    private UserResponse response;
    private UserServiceProxy mockUserServiceProxy;
    private UserPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new UserRequest(user1.getAlias());
        response = new UserResponse(true, "User returned", user1);

        // Create a mock UserService
        mockUserServiceProxy = Mockito.mock(UserServiceProxy.class);
        Mockito.when(mockUserServiceProxy.serve(request)).thenReturn(response);

        // Wrap a UserPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new UserPresenter(new UserPresenter.View() {}));
        Mockito.when(presenter.getUserService()).thenReturn(mockUserServiceProxy);
    }

    @Test
    public void testGetUser_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserServiceProxy.serve(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getUser(request));
    }

    @Test
    public void testGetUser_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserServiceProxy.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getUser(request);
        });
    }
}
