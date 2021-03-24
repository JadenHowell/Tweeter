package edu.byu.cs.tweeter.shared.domain;

import java.io.Serializable;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    private String userAlias;
    private String token;

    private AuthToken(){}

    public AuthToken(String userAlias, String token){
        this.userAlias = userAlias;
        this.token = token;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
