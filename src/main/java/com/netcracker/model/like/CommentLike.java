package com.netcracker.model.like;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.CommentConstant;

@ObjectType(LikeConstant.LDL_TYPE)
public class CommentLike extends AbstractLike {
    @Reference(value = CommentConstant.COM_LIKEDISLIKE, isParentChild = 0)
    private AbstractComment likedDislikedComment;

    public CommentLike() {
    }

    public CommentLike(String name) {
        super(name);
    }

    public CommentLike(String name, String description) {
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
        return "CommentLike{" +
                "likedDislikedComment=" + likedDislikedComment +
                '}';
    }
}
