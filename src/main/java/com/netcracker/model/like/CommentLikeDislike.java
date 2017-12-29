package com.netcracker.model.like;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.CommentConstant;

@ObjectType(LikeConstant.LDL_TYPE)
public class CommentLikeDislike extends AbstractLikeDislike {
    @Reference(CommentConstant.COM_LIKEDISLIKE)
    private AbstractComment likedDislikedComment;

    public CommentLikeDislike() {
    }

    public CommentLikeDislike(String name) {
        super(name);
    }

    public CommentLikeDislike(String name, String description) {
        super(name, description);
    }

    public AbstractComment getLikedDislikedComment() {
        return likedDislikedComment;
    }

    public void setLikedDislikedComment(AbstractComment likedDislikedComment) {
        this.likedDislikedComment = likedDislikedComment;
    }

    @Override
    public String toString() {
        return "CommentLikeDislike{" +
                "likedDislikedComment=" + likedDislikedComment +
                '}';
    }
}
