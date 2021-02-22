package edu.byu.cs.tweeter.model.service.request;

public class UserRequest extends Request{
    private final String userAlias;

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user who is to be returned.
     */
    public UserRequest(String userAlias){
        this.userAlias = userAlias;
    }

    /**
     * Returns the user alias of the user who is to be returned by this request.
     *
     * @return the user alias.
     */
    public String getUserAlias(){return userAlias;}
}
