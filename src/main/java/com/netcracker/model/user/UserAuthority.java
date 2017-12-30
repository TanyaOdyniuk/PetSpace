package com.netcracker.model.user;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.category.Category;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@ObjectType(UsersProfileConstant.USER_TYPE)
public class UserAuthority extends BaseEntity implements GrantedAuthority {

    @Attribute(UsersProfileConstant.USERTYPE_NAME)
    private String authority;
    /*@Reference(UsersProfileConstant.USERTYPE_TYPECAT)
    private Set<Category> categories;
    @Reference(UsersProfileConstant.USER_UTYPE)
    private List<User> users;
    */
    public UserAuthority() {
    }

    public UserAuthority(String name) {
        super(name);
    }

    public UserAuthority(String name, String description) {
        super(name, description);
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
/*
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
*/
    @Override
    public String toString() {
        return "UserAuthority{" +
                "authority='" + authority /*+ '\'' +
                ", categories=" + categories +
                ", users=" + users */+
                '}';
    }
}
