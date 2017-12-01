package com.netcracker.model.category;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.user.UserAuthority;

import java.util.List;
import java.util.Set;

@ObjectType(CategoryConstant.CAT_TYPE)
public class Category extends BaseEntity {

    @Attribute(CategoryConstant.CAT_NAME)
    private String categoryName;
    @Attribute(CategoryConstant.CAT_USERTYPE)
    private Set<UserAuthority> categoryUserTypes;
    @Reference(AdvertisementConstant.AD_CATEGORY)
    private List<Advertisement> categoryAds;

    public Category() {
    }

    public Category(String name) {
        super(name);
    }

    public Category(String name, String description) {
        super(name, description);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<UserAuthority> getCategoryUserTypes() {
        return categoryUserTypes;
    }

    public void setCategoryUserTypes(Set<UserAuthority> categoryUserTypes) {
        this.categoryUserTypes = categoryUserTypes;
    }

    public List<Advertisement> getCategoryAds() {
        return categoryAds;
    }

    public void setCategoryAds(List<Advertisement> categoryAds) {
        this.categoryAds = categoryAds;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryUserTypes=" + categoryUserTypes +
                ", categoryAds=" + categoryAds +
                '}';
    }
}
