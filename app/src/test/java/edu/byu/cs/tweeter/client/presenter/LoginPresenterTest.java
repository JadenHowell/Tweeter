package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.service.response.LoginResponse;

public class LoginPresenterTest {

    private LoginRequest request;
    private LoginResponse response;
    private LoginServiceProxy mockLoginService;
    private LoginPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        request = new LoginRequest("@TestUser", "password");
        response = new LoginResponse(new User("first2", "last2", "user", "url"), new AuthToken("@TestUser", "nonsenseToken"));

        mockLoginService = Mockito.mock(LoginServiceProxy.class);
        Mockito.when(mockLoginService.serve(request)).thenReturn(response);

        presenter = Mockito.spy(new LoginPresenter(new LoginPresenter.View() {}));
        Mockito.when(presenter.getLoginService()).thenReturn(mockLoginService);
    }

    @Test
    public void testLogin_returnsLoginResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockLoginService.serve(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.login(request));
    }

    @Test
    public void testLogin_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockLoginService.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.login(request);
        });
    }
}
