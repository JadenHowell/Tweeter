package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowerCountServiceProxy;
import edu.byu.cs.tweeter.client.model.service.FollowingCountServiceProxy;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.shared.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.shared.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.shared.service.response.FollowingCountResponse;

public class CountPresenterTest {
    private FollowingCountRequest followingCountRequest;
    private FollowingCountResponse followingCountResponse;
    private FollowingCountServiceProxy mockFollowingCountServiceProxy;
    private CountPresenter presenter;

    private FollowerCountRequest followerCountRequest;
    private FollowerCountResponse followerCountResponse;
    private FollowerCountServiceProxy mockFollowerCountServiceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        followingCountRequest = new FollowingCountRequest("@TestUser");
        followingCountResponse = new FollowingCountResponse(true, null, 7);

        followerCountRequest = new FollowerCountRequest("@TestUser");
        followerCountResponse = new FollowerCountResponse(true, null, 7);

        mockFollowerCountServiceProxy = Mockito.mock(FollowerCountServiceProxy.class);
        Mockito.when(mockFollowerCountServiceProxy.serve(followerCountRequest)).thenReturn(followerCountResponse);

        // Create a mock FollowingService
        mockFollowingCountServiceProxy = Mockito.mock(FollowingCountServiceProxy.class);
        Mockito.when(mockFollowingCountServiceProxy.serve(followingCountRequest)).thenReturn(followingCountResponse);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new CountPresenter(new CountPresenter.View() {}));
        Mockito.when(presenter.getFollowerCountService()).thenReturn(mockFollowerCountServiceProxy);
        Mockito.when(presenter.getFollowingCountService()).thenReturn(mockFollowingCountServiceProxy);
    }

    @Test
    public void testGetFollowingCount_returnsCorrectResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowingCountServiceProxy.serve(followingCountRequest)).thenReturn(followingCountResponse);
        Assertions.assertEquals(followingCountResponse, presenter.getFollowingCount(followingCountRequest));
    }

    @Test
    public void testGetFollowingCount_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowingCountServiceProxy.serve(followingCountRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollowingCount(followingCountRequest);
        });
    }

    @Test
    public void testGetFollowerCount_returnsCorrectResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowerCountServiceProxy.serve(followerCountRequest)).thenReturn(followerCountResponse);
        Assertions.assertEquals(followerCountResponse, presenter.getFollowerCount(followerCountRequest));
    }

    @Test
    public void testGetFollowerCount_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowerCountServiceProxy.serve(followerCountRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollowerCount(followerCountRequest);
        });
    }
}
