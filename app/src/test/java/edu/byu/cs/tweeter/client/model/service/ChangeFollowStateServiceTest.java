package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.service.request.ChangeFollowStateRequest;
import edu.byu.cs.tweeter.shared.service.response.ChangeFollowStateResponse;

public class ChangeFollowStateServiceTest {

    private ChangeFollowStateRequest validRequest;
    private ChangeFollowStateRequest invalidRequest;

    private ChangeFollowStateResponse successResponse;
    private ChangeFollowStateResponse failureResponse;

    private ChangeFollowStateService changeFollowStateServiceSpy;

    /**
     * Create a ChangeFollowStateService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        // Setup request objects to use in the tests
        validRequest = new ChangeFollowStateRequest("@TestUser", "@OtherUser");
        invalidRequest = new ChangeFollowStateRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new ChangeFollowStateResponse(true, null, true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.changeFollowState(validRequest)).thenReturn(successResponse);

        failureResponse = new ChangeFollowStateResponse(false, "An exception occurred", false);
        Mockito.when(mockServerFacade.changeFollowState(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        changeFollowStateServiceSpy = Mockito.spy(new ChangeFollowStateService());
        Mockito.when(changeFollowStateServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the ChangeFollowStateService.serve(ChangeFollowStateRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testChangeFollowState_validRequest_correctResponse() throws IOException {
        ChangeFollowStateResponse response = (ChangeFollowStateResponse) changeFollowStateServiceSpy.serve(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the ChangeFollowStateService.serveChangeFollowStateRequest)
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testChangeFollowState_invalidRequest_returnsInvalidResponse() throws IOException {
        ChangeFollowStateResponse response = (ChangeFollowStateResponse) changeFollowStateServiceSpy.serve(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
