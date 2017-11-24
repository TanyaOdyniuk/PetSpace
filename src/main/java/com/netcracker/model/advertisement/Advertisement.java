package com.netcracker.model.advertisement;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.Status;
import com.netcracker.model.category.Category;
import com.netcracker.model.comment.Comment;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;

import java.util.Date;
import java.util.List;
import java.util.Set;

@ObjectType(value = 2)
public class Advertisement extends BaseEntity {
    @Attribute(value = 6)
    private Date adDate;
    @Attribute(value = 7)
    private String adTopic;
    @Attribute(value = 8)
    private List<String> adPetSigns;
    @Boolean(value = 9, yesno = "yes")
    private boolean adIsVip;
    @Attribute(value = 10)
    private String adLocation;
    @Attribute(value = 11)
    private String adBasicInfo;
    @Attribute(value = 14)
    private Profile adAuthor;
    @Attribute(value = 13)
    private Status adStatus;
    @Attribute(value = 12)
    private Category adCategory;
    @Attribute(value = 15)
    private Set<Pet> adPets;
    //TODO SERVICE GET ADCOMMENTS
    @Reference(value = 401)// нет ничего более конкретного
    private List<Comment> advertisementComments;

    public Advertisement() {
    }

    public Advertisement(String name) {
        super(name);
    }

    public Advertisement(String name, String description) {
        super(name, description);
    }

    public Date getAdDate() {
        return adDate;
    }

    public void setAdDate(Date adDate) {
        this.adDate = adDate;
    }

    public String getAdTopic() {
        return adTopic;
    }

    public void setAdTopic(String adTopic) {
        this.adTopic = adTopic;
    }

    public List<String> getAdPetSigns() {
        return adPetSigns;
    }

    public void setAdPetSigns(List<String> adPetSigns) {
        this.adPetSigns = adPetSigns;
    }

    public boolean isAdIsVip() {
        return adIsVip;
    }

    public void setAdIsVip(boolean adIsVip) {
        this.adIsVip = adIsVip;
    }

    public String getAdLocation() {
        return adLocation;
    }

    public void setAdLocation(String adLocation) {
        this.adLocation = adLocation;
    }

    public String getAdBasicInfo() {
        return adBasicInfo;
    }

    public void setAdBasicInfo(String adBasicInfo) {
        this.adBasicInfo = adBasicInfo;
    }

    public Profile getAdAuthor() {
        return adAuthor;
    }

    public void setAdAuthor(Profile adAuthor) {
        this.adAuthor = adAuthor;
    }

    public Status getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(Status adStatus) {
        this.adStatus = adStatus;
    }

    public Category getAdCategory() {
        return adCategory;
    }

    public void setAdCategory(Category adCategory) {
        this.adCategory = adCategory;
    }

    public Set<Pet> getAdPets() {
        return adPets;
    }

    public void setAdPets(Set<Pet> adPets) {
        this.adPets = adPets;
    }

    public List<Comment> getAdvertisementComments() {
        return advertisementComments;
    }

    public void setAdvertisementComments(List<Comment> advertisementComments) {
        this.advertisementComments = advertisementComments;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "adDate=" + adDate +
                ", adTopic='" + adTopic + '\'' +
                ", adPetSigns=" + adPetSigns +
                ", adIsVip=" + adIsVip +
                ", adLocation='" + adLocation + '\'' +
                ", adBasicInfo='" + adBasicInfo + '\'' +
                ", adAuthor=" + adAuthor +
                ", adStatus=" + adStatus +
                ", adCategory=" + adCategory +
                ", adPets=" + adPets +
                ", advertisementComments=" + advertisementComments +
                '}';
    }
}
