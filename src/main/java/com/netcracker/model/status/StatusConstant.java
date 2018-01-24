package com.netcracker.model.status;

public interface StatusConstant {
    int ST_TYPE = 3;
    int ST_NAME = 16;
    int ST_IS_NOT_FRIEND = 4;
    int ST_IS_FRIEND = 5;
    int ST_IS_PENDING = 6;
    int ST_IS_BLACKLIST = 7;

    String IS_NOT_FRIEND = "notfriend";
    String IS_FRIEND = "isfriend";
    String IS_PENDING = "pending";
    String IS_BLACKLIST = "ignored";
}
