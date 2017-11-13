package com.netcracker.model.securityBook;

import java.util.List;

public enum SecurityType {
    SECURITY_TYPE_ONE("Service type one"),
    SECURITY_TYPE_TWO("Service type two");

    private String type;
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
