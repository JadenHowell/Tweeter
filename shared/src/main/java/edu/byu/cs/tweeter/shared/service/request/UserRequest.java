package edu.byu.cs.tweeter.shared.service.request;

import edu.byu.cs.tweeter.shared.domain.AuthToken;

public class UserRequest extends Request{
    private String userAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private UserRequest() {}

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user who is to be returned.
     */
    public UserRequest(String userAlias, AuthToken authToken){
        super(authToken);
        this.userAlias = userAlias;
    }

    /**
     * Returns the user alias of the user who is to be returned by this request.
     *
     * @return the user alias.
     */
    public String getUserAlias(){return userAlias;}

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
