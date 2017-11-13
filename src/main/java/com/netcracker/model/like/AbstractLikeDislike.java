package com.netcracker.model.like;

import com.netcracker.model.Model;
import com.netcracker.model.user.User;

import java.util.Date;

public class AbstractLikeDislike extends Model {
    private Date likeDislikeDate;
    private boolean isDislike;
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

    public void setDislike(boolean dislike) {
        isDislike = dislike;
    }

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
