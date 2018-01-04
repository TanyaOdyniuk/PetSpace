package com.netcracker.model.status;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.request.FriendRequest;
import com.netcracker.model.request.FriendRequestConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.UsersProfileConstant;

import java.util.List;

@ObjectType(StatusConstant.ST_TYPE)
public class Status extends BaseEntity {

    @Attribute(StatusConstant.ST_NAME)
    private String statusName;

    public Status() {
    }

    public Status(String name) {
        super(name);
    }

    public Status(String name, String description) {
        super(name, description);
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return "Status{" +
                "statusName='" + statusName + '\'' +
                '}';
    }
}
