package com.netcracker.model.user;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.category.Category;

import java.util.List;
import java.util.Set;

@ObjectType(UsersProfileConstant.USERTYPE_TYPE)
public class UserType extends BaseEntity {

    @Attribute(UsersProfileConstant.USERTYPE_NAME)
    private String type;
    @Attribute(UsersProfileConstant.USERTYPE_TYPECAT)
    private Set<Category> categories;
    @Reference(UsersProfileConstant.USER_UTYPE)
    private List<User> users;

    public UserType() {
    }

    public UserType(String name) {
        super(name);
    }

    public UserType(String name, String description) {
        super(name, description);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "type='" + type + '\'' +
                ", categories=" + categories +
                ", users=" + users +
                '}';
    }
}
