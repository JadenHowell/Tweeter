package edu.byu.cs.tweeter.shared.service.request;

public class RegisterRequest extends Request {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String photoURL;
    private byte[] photo;

    private RegisterRequest() {
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
    public byte[] getPhoto() {
        return photo;
    }
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    public String getPhotoURL() {
        return photoURL;
    }
}
