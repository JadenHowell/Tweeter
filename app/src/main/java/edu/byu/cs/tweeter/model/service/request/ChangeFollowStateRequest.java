package edu.byu.cs.tweeter.model.service.request;

public class ChangeFollowStateRequest extends Request{
    private final String rootUserAlias;
    private final String otherUserAlias;

    public ChangeFollowStateRequest(String rootUserAlias, String otherUserAlias){
        this.rootUserAlias = rootUserAlias;
        this.otherUserAlias = otherUserAlias;
    }

    public String getRootUserAlias(){return rootUserAlias;}

    public String getOtherUserAlias() {return otherUserAlias;}
}
