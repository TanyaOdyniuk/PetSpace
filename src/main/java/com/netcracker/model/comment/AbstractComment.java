package com.netcracker.model.comment;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.status.Status;
import com.netcracker.model.user.Profile;

import java.sql.Timestamp;

public class AbstractComment extends BaseEntity {
    @Attribute(CommentConstant.COM_INFO)
    private String commentText;
    @Attribute(CommentConstant.COM_DATE)
    private Timestamp commentDate;
    @Reference(value = CommentConstant.COM_AUTOR, isParentChild = 0)
    private Profile commentAuthor;
    @Reference(value = CommentConstant.COM_STATE, isParentChild = 0)
    private Status commentState;

    public AbstractComment() {
    }

    public AbstractComment(String name) {
        super(name);
    }

    public AbstractComment(String name, String description) {
        super(name, description);
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Timestamp commentDate) {
        this.commentDate = commentDate;
    }

    public Profile getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(Profile commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public Status getCommentState() {
        return commentState;
    }

    public void setCommentState(Status commentState) {
        this.commentState = commentState;
    }

    @Override
    public String toString() {
        return "AbstractComment{" +
                "commentText='" + commentText + '\'' +
                ", commentDate=" + commentDate +
                ", commentAuthor=" + commentAuthor +
                ", commentState=" + commentState +
                '}';
    }
}
