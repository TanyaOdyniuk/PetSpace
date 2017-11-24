package com.netcracker.model.securityBook;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;

import java.util.List;

@ObjectType(value = 205)
public enum SecurityType {
    SECURITY_TYPE_ONE("Service type one"),
    SECURITY_TYPE_TWO("Service type two");

    @Attribute(value = 211)
    private String type;
    @Reference(value = 306)
    private List<SecurityBook> securityBooks;

    SecurityType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<SecurityBook> getSecurityBooks() {
        return securityBooks;
    }

    public void setSecurityBooks(List<SecurityBook> securityBooks) {
        this.securityBooks = securityBooks;
    }
}
