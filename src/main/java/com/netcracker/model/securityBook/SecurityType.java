package com.netcracker.model.securityBook;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.List;

@ObjectType(value = 205)
public  class SecurityType extends BaseEntity {

    @Attribute(value = 211)
    private String securityType;
    @Reference(value = 306)
    private List<SecurityBook> securityBooks;

    public SecurityType() {
    }

    public SecurityType(String name) {
        super(name);
    }

    public SecurityType(String name, String description) {
        super(name, description);
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public List<SecurityBook> getSecurityBooks() {
        return securityBooks;
    }

    public void setSecurityBooks(List<SecurityBook> securityBooks) {
        this.securityBooks = securityBooks;
    }

    @Override
    public String toString() {
        return "SecurityType{" +
                "securityType='" + securityType + '\'' +
                ", securityBooks=" + securityBooks +
                '}';
    }
}
