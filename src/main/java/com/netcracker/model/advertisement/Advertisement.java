package com.netcracker.model.advertisement;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.status.Status;
import com.netcracker.model.category.Category;
import com.netcracker.model.comment.AdvertisementComment;
import com.netcracker.model.comment.CommentConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@ObjectType(AdvertisementConstant.AD_TYPE)
public class Advertisement extends BaseEntity {
    @Attribute(AdvertisementConstant.AD_DATE)
    private Timestamp adDate;
    @Attribute(AdvertisementConstant.AD_TOPIC)
    private String adTopic;
    @Attribute(AdvertisementConstant.AD_PET_SIGNS)
    private List<String> adPetSigns;
    @Boolean(value = AdvertisementConstant.AD_IS_VIP, yesno = AdvertisementConstant.AD_IS_VIP_STRING)
    private java.lang.Boolean adIsVip;
    @Attribute(AdvertisementConstant.AD_LOCATION)
    private String adLocation;
    @Attribute(AdvertisementConstant.AD_BASIC_INFO)
    private String adBasicInfo;
    @Reference(value = AdvertisementConstant.AD_AUTHOR, isParentChild = 0)
    private Profile adAuthor;
    @Reference(value = AdvertisementConstant.AD_STATUS, isParentChild = 0)
    private Status adStatus;
    @Reference(value = AdvertisementConstant.AD_CATEGORY, isParentChild = 0)
    private Category adCategory;
    @Reference(value = AdvertisementConstant.AD_PETS, isParentChild = 1)
    private Set<Pet> adPets;

    public Advertisement() {
    }

    public Advertisement(String name) {
        super(name);
    }

    public Advertisement(String name, String description) {
        super(name, description);
    }

    public Timestamp getAdDate() {
        return adDate;
    }

    public void setAdDate(Timestamp adDate) {
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

    public java.lang.Boolean isAdIsVip() {
        return adIsVip;
    }

    public void setAdIsVip(java.lang.Boolean adIsVip) {
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
                '}';
    }
}
