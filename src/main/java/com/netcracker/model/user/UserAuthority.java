package com.netcracker.model.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.category.Category;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

@ObjectType(UsersProfileConstant.USERTYPE_TYPE)
public class UserAuthority extends BaseEntity implements GrantedAuthority {

    @Attribute(UsersProfileConstant.USERTYPE_NAME)
    private String authority;

    public UserAuthority() {
        super();
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

    @Override
    public String toString() {
        return "UserAuthority{" +
                "authority='" + authority + '\''+
                '}';
    }
}
