package com.netcracker.model.comment;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.like.AbstractLikeDislike;
import com.netcracker.model.user.Profile;

import java.util.List;

@ObjectType(CommentConstant.COM_TYPE)
public abstract class AbstractComment extends BaseEntity {
    @Attribute(CommentConstant.COM_INFO)
    private String commentText;
    @Attribute(CommentConstant.COM_DATE)
    private String commentDate;
    @Reference(CommentConstant.COM_AUTOR)
    private Profile commentAuthor;
    //TODO SERVICE GET LIKES
    @Reference(CommentConstant.COM_LIKEDISLIKE)
    private List<AbstractLikeDislike> commentLikes;
    //TODO SERVICE GET DISLIKES
    @Reference(CommentConstant.COM_LIKEDISLIKE)
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

    public Profile getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(Profile commentAuthor) {
        this.commentAuthor = commentAuthor;
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
                ", commentAuthor=" + commentAuthor +
                ", commentLikes=" + commentLikes +
                ", commentDislikes=" + commentDislikes +
                '}';
    }
}
