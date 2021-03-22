package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.ChangeFollowStateServiceProxy;
import edu.byu.cs.tweeter.client.model.service.IsFollowingServiceProxy;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;
import edu.byu.cs.tweeter.shared.service.response.IsFollowingResponse;

public class FollowButtonPresenterTest {
    private IsFollowingRequest request;
    private IsFollowingResponse response;
    private IsFollowingServiceProxy mockIsFollowingServiceProxy;
    private FollowButtonPresenter presenter;

    private ChangeFollowStateRequest changeStateRequest;
    private ChangeFollowStateResponse changeStateResponse;
    private ChangeFollowStateServiceProxy mockChangeStateService;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        request = new IsFollowingRequest("@TestUser", "@OtherUser");
        response = new IsFollowingResponse(true, null, true);

        changeStateRequest = new ChangeFollowStateRequest("@TestUser", "@OtherUser");
        changeStateResponse = new ChangeFollowStateResponse(true, null, true);

        mockChangeStateService = Mockito.mock(ChangeFollowStateServiceProxy.class);
        Mockito.when(mockChangeStateService.serve(changeStateRequest)).thenReturn(changeStateResponse);

        // Create a mock FollowingService
        mockIsFollowingServiceProxy = Mockito.mock(IsFollowingServiceProxy.class);
        Mockito.when(mockIsFollowingServiceProxy.serve(request)).thenReturn(response);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new FollowButtonPresenter(new FollowButtonPresenter.View() {}));
        Mockito.when(presenter.getIsFollowingService()).thenReturn(mockIsFollowingServiceProxy);
        Mockito.when(presenter.getChangeFollowStateService()).thenReturn(mockChangeStateService);
    }

    @Test
    public void testGetIsFollowing_returnsCorrectResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockIsFollowingServiceProxy.serve(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.getIsFollowing(request));
    }

    @Test
    public void testGetIsFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockIsFollowingServiceProxy.serve(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getIsFollowing(request);
        });
    }

    @Test
    public void testChangeState_returnsCorrectResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockChangeStateService.serve(changeStateRequest)).thenReturn(changeStateResponse);
        Assertions.assertEquals(changeStateResponse, presenter.changeFollowState(changeStateRequest));
    }

    @Test
    public void testChangeState_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockChangeStateService.serve(changeStateRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.changeFollowState(changeStateRequest);
        });
    }
}
