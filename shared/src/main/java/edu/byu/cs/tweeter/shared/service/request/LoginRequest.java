package edu.byu.cs.tweeter.shared.service.request;

/**
 * Contains all the information needed to make a login request.
 */
public class LoginRequest extends Request {

    private String username;
    private String password;

    public LoginRequest() {
        this.username = "user_alias";
        this.password = "password";
    }

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be logged in.
     * @param password the password of the user to be logged in.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
