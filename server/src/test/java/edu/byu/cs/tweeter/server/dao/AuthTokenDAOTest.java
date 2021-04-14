package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.service.response.LogoutResponse;

public class AuthTokenDAOTest {
    private AuthTokenDAO authTokenDAO;

    @BeforeEach
    public void setup(){
        authTokenDAO = new AuthTokenDAO();
    }

    @Test
    public void getAuthTokenSuccess() {
        AuthToken expectedResponse = new AuthToken("@TestUser", "token");
        AuthToken response = authTokenDAO.newAuthToken("@TestUser");
        Assertions.assertEquals(expectedResponse.getUserAlias(), response.getUserAlias());
        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals(response.getToken().getClass(), String.class);
    }

    @Test
    public void checkAuthTokenSuccess() {
        AuthToken authToken = authTokenDAO.newAuthToken("@TestUser");
        Boolean response = authTokenDAO.checkAuthToken(authToken);
        Assertions.assertTrue(response);
    }

    @Test
    public void checkAuthTokenFail() {
        AuthToken fakeAuthToken = new AuthToken("@TestUser", "token");
        authTokenDAO.logout(new LogoutRequest("@TestUser", fakeAuthToken));
        Boolean response = authTokenDAO.checkAuthToken(fakeAuthToken);
        Assertions.assertFalse(response);
    }

    @Test
    public void checkLogoutSuccess() {
        AuthToken fakeAuthToken = new AuthToken("@TestUser", "token");
        LogoutResponse expectedResponse = new LogoutResponse(true, "Good job, you logged out");
        LogoutResponse response = authTokenDAO.logout(new LogoutRequest("@TestUser", fakeAuthToken));
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
}
