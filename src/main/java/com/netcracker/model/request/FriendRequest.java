package com.netcracker.model.request;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.Status;
import com.netcracker.model.user.Profile;

@ObjectType(FriendRequestConstant.REQ_TYPE)
public class FriendRequest extends BaseEntity{

    @Reference(FriendRequestConstant.REQ_FROM)
    private Profile reqFrom;
    @Reference(FriendRequestConstant.REQ_TO)
    private Profile reqTo;
    @Reference(FriendRequestConstant.REQ_STATUS)
    private Status requestStatus;

    public Profile getReqFrom() {
        return reqFrom;
    }

    public void setReqFrom(Profile reqFrom) {
        this.reqFrom = reqFrom;
    }

    public Profile getReqTo() {
        return reqTo;
    }

    public void setReqTo(Profile reqTo) {
        this.reqTo = reqTo;
    }

    public Status getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Status requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "reqFrom=" + reqFrom +
                ", reqTo=" + reqTo +
                ", requestStatus=" + requestStatus +
                '}';
    }
}
