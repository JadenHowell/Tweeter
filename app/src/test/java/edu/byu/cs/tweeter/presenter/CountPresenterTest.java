package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowerCountService;
import edu.byu.cs.tweeter.model.service.FollowingCountService;
import edu.byu.cs.tweeter.model.service.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingCountResponse;

public class CountPresenterTest {
    private FollowingCountRequest followingCountRequest;
    private FollowingCountResponse followingCountResponse;
    private FollowingCountService mockFollowingCountService;
    private CountPresenter presenter;

    private FollowerCountRequest followerCountRequest;
    private FollowerCountResponse followerCountResponse;
    private FollowerCountService mockFollowerCountService;

    @BeforeEach
    public void setup() throws IOException {
        followingCountRequest = new FollowingCountRequest("@TestUser");
        followingCountResponse = new FollowingCountResponse(true, null, 7);

        followerCountRequest = new FollowerCountRequest("@TestUser");
        followerCountResponse = new FollowerCountResponse(true, null, 7);

        mockFollowerCountService = Mockito.mock(FollowerCountService.class);
        Mockito.when(mockFollowerCountService.serve(followerCountRequest)).thenReturn(followerCountResponse);

        // Create a mock FollowingService
        mockFollowingCountService = Mockito.mock(FollowingCountService.class);
        Mockito.when(mockFollowingCountService.serve(followingCountRequest)).thenReturn(followingCountResponse);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new CountPresenter(new CountPresenter.View() {}));
        Mockito.when(presenter.getFollowerCountService()).thenReturn(mockFollowerCountService);
        Mockito.when(presenter.getFollowingCountService()).thenReturn(mockFollowingCountService);
    }

    @Test
    public void testGetFollowingCount_returnsCorrectResult() throws IOException {
        Mockito.when(mockFollowingCountService.serve(followingCountRequest)).thenReturn(followingCountResponse);
        Assertions.assertEquals(followingCountResponse, presenter.getFollowingCount(followingCountRequest));
    }

    @Test
    public void testGetFollowingCount_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowingCountService.serve(followingCountRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollowingCount(followingCountRequest);
        });
    }

    @Test
    public void testGetFollowerCount_returnsCorrectResult() throws IOException {
        Mockito.when(mockFollowerCountService.serve(followerCountRequest)).thenReturn(followerCountResponse);
        Assertions.assertEquals(followerCountResponse, presenter.getFollowerCount(followerCountRequest));
    }

    @Test
    public void testGetFollowerCount_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowerCountService.serve(followerCountRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollowerCount(followerCountRequest);
        });
    }
}
