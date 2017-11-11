package com.netcracker.model;

public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getType() {
        return status;
    }
}
