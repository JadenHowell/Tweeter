package edu.byu.cs.tweeter.model.service.response;


import edu.byu.cs.tweeter.model.domain.User;

public class UserResponse extends Response{
    User user;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param success a boolean that represents the request was successful.
     * @param message a message describing why the request was successful.
     * @param user the user to be returned in the response.
     */
    public UserResponse(boolean success, String message, User user){
        super(success, message);
        this.user = user;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param success a boolean that represents the request was unsuccessful.
     * @param message a message describing why the request was unsuccessful.
     */
    public UserResponse(boolean success, String message){
        super(success, message);
    }

    public User getUser(){return user;}
}
