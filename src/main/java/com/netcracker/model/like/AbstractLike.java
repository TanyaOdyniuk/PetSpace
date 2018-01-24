package com.netcracker.model.like;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.Profile;

import java.sql.Timestamp;

public class AbstractLike extends BaseEntity {
    @Attribute(value = LikeConstant.LDL_ISDISLIKE)
    private String isDislike;

    public AbstractLike() {
    }

    public AbstractLike(String name) {
        super(name);
    }

    public AbstractLike(String name, String description) {
        super(name, description);
    }

    public String getIsDislike() {
        return isDislike;
    }

    public void setIsDislike(String dislike) {
        this.isDislike = dislike;
    }

    @Override
    public String toString() {
        return "AbstractLike{" +
                ", isDislike=" + isDislike +
                '}';
    }
}
