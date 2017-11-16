package com.netcracker.model.advertisement;

import com.netcracker.model.Model;
import com.netcracker.model.Status;
import com.netcracker.model.category.Category;
import com.netcracker.model.comment.AdvertisementComment;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Advertisement extends Model {
    private Date adDate;
    private String adTopic;
    private List<String> adPetSigns;
    private Boolean adIsVip;
    private String adLocation;
    private String adBasicInfo;
    private User adUser;
    private Status adStatus;
    private Category adCategory;
    private Set<Pet> adPets;
    private List<AdvertisementComment> advertisementComments;

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

    public User getAdUser() {
        return adUser;
    }

    public void setAdUser(User adUser) {
        this.adUser = adUser;
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

    public List<AdvertisementComment> getAdvertisementComments() {
        return advertisementComments;
    }

    public void setAdvertisementComments(List<AdvertisementComment> advertisementComments) {
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
                ", adUser=" + adUser +
                ", adStatus=" + adStatus +
                ", adCategory=" + adCategory +
                ", adPets=" + adPets +
                ", advertisementComments=" + advertisementComments +
                '}';
    }
}
