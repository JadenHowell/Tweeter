package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.service.response.RegisterResponse;

public class RegisterPresenterTest {

    private RegisterRequest request;
    private RegisterResponse response;
    private RegisterServiceProxy mockRegisterService;
    private RegisterPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        request = new RegisterRequest("first", "last", "username", "password");
        response = new RegisterResponse(new User("first", "last", "url"), new AuthToken("@TestUser", "nonsenseToken"));

        mockRegisterService = Mockito.mock(RegisterServiceProxy.class);
        Mockito.when(mockRegisterService.serve(request)).thenReturn(response);

        presenter = Mockito.spy(new RegisterPresenter(new RegisterPresenter.View() {}));
        Mockito.when(presenter.getRegisterService()).thenReturn(mockRegisterService);
    }

    @Test
    public void testRegister_returnsRegisterResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockRegisterService.serve(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.register(request));
    }

    @Test
    public void testRegister_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockRegisterService.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.register(request);
        });
    }
}
