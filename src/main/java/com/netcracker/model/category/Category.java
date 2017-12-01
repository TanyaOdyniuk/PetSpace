package com.netcracker.model.category;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.user.UserAuthority;

import java.util.List;
import java.util.Set;

@ObjectType(value = 4)
public class Category extends BaseEntity {

    @Attribute(value = 17)
    private String categoryName;
    @Attribute(value = 18)
    private Set<UserAuthority> categoryUserAuthorities;
    @Reference(value = 12)
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

    public Set<UserAuthority> getCategoryUserAuthorities() {
        return categoryUserAuthorities;
    }

    public void setCategoryUserAuthorities(Set<UserAuthority> categoryUserAuthorities) {
        this.categoryUserAuthorities = categoryUserAuthorities;
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
                ", categoryUserAuthorities=" + categoryUserAuthorities +
                ", categoryAds=" + categoryAds +
                '}';
    }
}
