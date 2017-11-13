package com.netcracker.model.securityBook;

import com.netcracker.model.Model;
import com.netcracker.model.user.User;

import java.util.Set;

public class SecurityBook extends Model {

    private Object attribute;
    private SecurityType securityType;
    private Set<User> users;

    public SecurityBook() {
    }

    public SecurityBook(String name) {
        super(name);
    }

    public SecurityBook(String name, String description) {
        super(name, description);
    }

    public Object getAttribute() {
        return attribute;
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
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
                "attribute=" + attribute +
                ", securityType=" + securityType +
                ", users=" + users +
                '}';
    }
}
