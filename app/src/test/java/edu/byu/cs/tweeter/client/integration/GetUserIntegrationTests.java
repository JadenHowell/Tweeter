package edu.byu.cs.tweeter.client.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.shared.domain.User;
import edu.byu.cs.tweeter.shared.net.TweeterRemoteException;
import edu.byu.cs.tweeter.shared.service.request.UserRequest;
import edu.byu.cs.tweeter.shared.service.response.UserResponse;

public class GetUserIntegrationTests {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User testUser = new User("Test", "User", MALE_IMAGE_URL);
    private Service userService;

    @BeforeEach
    public void setup(){
        userService = new UserServiceProxy();
    }

    @Test
    public void postSuccess() throws IOException, TweeterRemoteException {
        UserResponse expectedResponse = new UserResponse(true, "User returned", testUser);
        UserRequest request = new UserRequest("@TestUser");
        UserResponse response = (UserResponse) userService.serve(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }

    @Test
    public void postFail() throws IOException, TweeterRemoteException {
        UserResponse expectedResponse = new UserResponse(true, "User \"@fakeuser\" not found");
        UserRequest request = new UserRequest("@fakeuser");
        UserResponse response = (UserResponse) userService.serve(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
}
