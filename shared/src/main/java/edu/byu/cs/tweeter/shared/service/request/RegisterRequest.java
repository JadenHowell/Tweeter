package edu.byu.cs.tweeter.shared.service.request;

public class RegisterRequest extends Request {

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public RegisterRequest() {
        this.firstName = "dummy_first_name";
        this.lastName = "dummy_last_name";
        this.username = "dummy_username";
        this.password = "dummy_password";
    }

    public RegisterRequest(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
