package com.netcracker.model.securitybook;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.List;

@ObjectType(SecurityBookConstant.SECT_TYPE)
public  class SecurityType extends BaseEntity {

    @Attribute(SecurityBookConstant.SECT_NAME)
    private String securityType;

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
    @Override
    public String toString() {
        return "SecurityType{" +
                "securityType='" + securityType + '\'' +
                '}';
    }
}
