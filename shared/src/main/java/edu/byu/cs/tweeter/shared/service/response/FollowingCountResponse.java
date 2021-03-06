package edu.byu.cs.tweeter.shared.service.response;

public class FollowingCountResponse extends Response{
    int count;

    public FollowingCountResponse(boolean success, String message, int count){
        super(success, message);
        this.count = count;
    }

    /**
     * A method to get the response count
     * @return the following count
     */
    public int getCount(){return count;}
}
