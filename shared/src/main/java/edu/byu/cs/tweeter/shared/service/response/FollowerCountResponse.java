package edu.byu.cs.tweeter.shared.service.response;

public class FollowerCountResponse extends Response{
    int count;

    public FollowerCountResponse(boolean success, String message, int count){
        super(success, message);
        this.count = count;
    }

    /**
     * A method to get the response count
     * @return the following count
     */
    public int getCount(){return count;}
}