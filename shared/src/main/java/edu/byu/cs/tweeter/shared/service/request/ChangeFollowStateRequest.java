package edu.byu.cs.tweeter.shared.service.request;

public class ChangeFollowStateRequest extends Request{
    private String rootUserAlias;
    private String otherUserAlias;

    public ChangeFollowStateRequest(String rootUserAlias, String otherUserAlias){
        this.rootUserAlias = rootUserAlias;
        this.otherUserAlias = otherUserAlias;
    }

    private ChangeFollowStateRequest(){}

    public String getRootUserAlias(){return rootUserAlias;}

    public void setRootUserAlias(String rootUserAlias) {
        this.rootUserAlias = rootUserAlias;
    }

    public String getOtherUserAlias() {return otherUserAlias;}

    public void setOtherUserAlias(String otherUserAlias) {
        this.otherUserAlias = otherUserAlias;
    }
}
