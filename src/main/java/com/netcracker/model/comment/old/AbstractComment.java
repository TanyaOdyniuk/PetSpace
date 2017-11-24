package com.netcracker.model.comment.old;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.User;

import java.util.Date;

@ObjectType(value = 401)
public abstract class AbstractComment extends BaseEntity {
    @Attribute(value = 403)
    private Date commentDate;
    @Attribute(value = 404)
    private String commentText;
    @Attribute(value = 406)
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
