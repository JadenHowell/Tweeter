package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class LogoutPresenterTest {

    private LogoutRequest request;
    private LogoutResponse response;
    private LogoutServiceProxy mockLogoutService;
    private LogoutPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        request = new LogoutRequest("@TestUser", new AuthToken("@TestUser", "nonsenseToken"));
        response = new LogoutResponse(true, "");

        mockLogoutService = Mockito.mock(LogoutServiceProxy.class);
        Mockito.when(mockLogoutService.serve(request)).thenReturn(response);

        presenter = Mockito.spy(new LogoutPresenter(new LogoutPresenter.View() {}));
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutService);
    }

    @Test
    public void testLogout_returnsLogoutResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockLogoutService.serve(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.logout(request));
    }

    @Test
    public void testLogout_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockLogoutService.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.logout(request);
        });
    }
}
