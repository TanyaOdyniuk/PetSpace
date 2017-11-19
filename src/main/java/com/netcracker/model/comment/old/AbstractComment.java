package com.netcracker.model.comment.old;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.User;

import java.util.Date;

public abstract class AbstractComment extends BaseEntity {
    private Date commentDate;
    private String commentText;
    private User user;

    public AbstractComment() {
    }

    public AbstractComment(String name) {
        super(name);
    }

    public AbstractComment(String name, String description) {
        super(name, description);
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AbstractComment{" +
                "commentDate=" + commentDate +
                ", commentText='" + commentText + '\'' +
                ", user=" + user +
                '}';
    }
}
