package com.netcracker.model.like;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.Profile;

import java.sql.Date;


@ObjectType(LikeConstant.LDL_TYPE)
public abstract class AbstractLikeDislike extends BaseEntity {
    @Attribute(LikeConstant.LDL_DATE)
    private Date likeDate;
    @Boolean(value = LikeConstant.LDL_ISDISLIKE, yesno = LikeConstant.LDL_IS_DISLIKE_STRING)
    private boolean isDislike;
    @Attribute(LikeConstant.LDL_AUTOR)
    private Profile likeAuthor;

    public AbstractLikeDislike() {
    }

    public AbstractLikeDislike(String name) {
        super(name);
    }

    public AbstractLikeDislike(String name, String description) {
        super(name, description);
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }

    public boolean getDislike() {
        return isDislike;
    }

    public void setDislike(boolean dislike) {
        isDislike = dislike;
    }

    public Profile getLikeAuthor() {
        return likeAuthor;
    }

    public void setLikeAuthor(Profile likeAuthor) {
        this.likeAuthor = likeAuthor;
    }

    @Override
    public String toString() {
        return "AbstractLikeDislike{" +
                "likeDate=" + likeDate +
                ", isDislike=" + isDislike +
                ", likeAuthor=" + likeAuthor +
                '}';
    }
}
