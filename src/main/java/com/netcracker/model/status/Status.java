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
    @Reference(GroupConstant.GR_STATUS)
    private List<Group> groups;
    @Reference(UsersProfileConstant.PROFILE_STATUS)
    private List<Profile> profiles;
    @Reference(AdvertisementConstant.AD_STATUS)
    private List<Advertisement> advertisements;
    @Reference(PetConstant.PET_STATE)
    private List<Pet> pets;
    @Reference(FriendRequestConstant.REQ_STATUS)
    private List<FriendRequest> requests;


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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<FriendRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<FriendRequest> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return "Status{" +
                "statusName='" + statusName + '\'' +
                ", groups=" + groups +
                ", profiles=" + profiles +
                ", advertisements=" + advertisements +
                ", pets=" + pets +
                ", requests=" + requests +
                '}';
    }
}
