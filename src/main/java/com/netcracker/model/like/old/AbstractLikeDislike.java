package com.netcracker.model.like.old;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.User;

import java.util.Date;

@ObjectType(value = 400)
public class AbstractLikeDislike extends BaseEntity {
    @Attribute(value = 400)
    private Date likeDislikeDate;
    @Boolean(value = 401, yesno = "yes")
    private boolean isDislike;
    @Attribute(value = 402)
    private User user;

    public AbstractLikeDislike() {
    }

    public AbstractLikeDislike(String name) {
        super(name);
    }

    public AbstractLikeDislike(String name, String description) {
        super(name, description);
    }

    public Date getLikeDislikeDate() {
        return likeDislikeDate;
    }

    public void setLikeDislikeDate(Date likeDislikeDate) {
        this.likeDislikeDate = likeDislikeDate;
    }

    public boolean isDislike() {
        return isDislike;
    }

    public void setDislike(boolean dislike) { isDislike = dislike; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AbstractLikeDislike{" +
                "likeDislikeDate=" + likeDislikeDate +
                ", isDislike=" + isDislike +
                ", user=" + user +
                '}';
    }
}
