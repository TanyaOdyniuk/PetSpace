package com.netcracker.model.like;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.Profile;

import java.sql.Date;


@ObjectType(value = 400)
public abstract class AbstractLikeDislike extends BaseEntity {
    @Attribute(value = 400)
    private Date likeDate;
    @Boolean(value = 401, yesno = "yes")
    private boolean isDislike;
    @Attribute(value = 402)
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
