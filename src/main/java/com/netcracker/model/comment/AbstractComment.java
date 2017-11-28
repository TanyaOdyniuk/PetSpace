package com.netcracker.model.comment;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.like.AbstractLikeDislike;
import com.netcracker.model.user.Profile;

import java.util.List;

public abstract class AbstractComment extends BaseEntity {
    @Attribute(value = 407)
    private String commentText;
    @Attribute(value = 408)
    private String commentDate;
    private Profile commentProfile;
    //TODO SERVICE GET LIKES
    private List<AbstractLikeDislike> commentLikes;
    //TODO SERVICE GET DISLIKES
    private List<AbstractLikeDislike> commentDislikes;

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

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public Profile getCommentProfile() {
        return commentProfile;
    }

    public void setCommentProfile(Profile commentProfile) {
        this.commentProfile = commentProfile;
    }

    public List<AbstractLikeDislike> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(List<AbstractLikeDislike> commentLikes) {
        this.commentLikes = commentLikes;
    }

    public List<AbstractLikeDislike> getCommentDislikes() {
        return commentDislikes;
    }

    public void setCommentDislikes(List<AbstractLikeDislike> commentDislikes) {
        this.commentDislikes = commentDislikes;
    }

    @Override
    public String toString() {
        return "AbstractComment{" +
                "commentText='" + commentText + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", commentProfile=" + commentProfile +
                ", commentLikes=" + commentLikes +
                ", commentDislikes=" + commentDislikes +
                '}';
    }
}
