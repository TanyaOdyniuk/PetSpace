package com.netcracker.model.request;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.status.Status;
import com.netcracker.model.user.Profile;

@ObjectType(FriendRequestConstant.REQ_TYPE)
public class FriendRequest extends BaseEntity{

    @Reference(value = FriendRequestConstant.REQ_FROM, isParentChild = 0)
    private Profile reqFrom;
    @Reference(value = FriendRequestConstant.REQ_TO, isParentChild = 0)
    private Profile reqTo;
    @Reference(value = FriendRequestConstant.REQ_STATUS, isParentChild = 0)
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
