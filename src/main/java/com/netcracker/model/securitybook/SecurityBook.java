package com.netcracker.model.securitybook;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;

import java.math.BigInteger;
import java.util.Set;

@ObjectType(SecurityBookConstant.SECB_TYPE)
public class SecurityBook extends BaseEntity {

    @Reference(SecurityBookConstant.SECB_USERSECB)
    private BigInteger attributeId;
    @Reference(SecurityBookConstant.SECT_TYPE)
    private SecurityType securityType;
    //TODO SERVICE TO GET USERS
    @Reference(UsersProfileConstant.USER_SECBOOK)
    private Set<User> users;

    public SecurityBook() {
    }

    public SecurityBook(String name) {
        super(name);
    }

    public SecurityBook(String name, String description) {
        super(name, description);
    }

    public BigInteger getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(BigInteger attributeId) {
        this.attributeId = attributeId;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "SecurityBook{" +
                "attribute=" + attributeId +
                ", securityType=" + securityType +
                ", users=" + users +
                '}';
    }
}
