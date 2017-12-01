package com.netcracker.model.like;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.CommentConstant;

@ObjectType(LikeConstant.LDL_TYPE)
public class CommentLikeDislike extends AbstractLikeDislike {
    @Reference(CommentConstant.COM_LIKEDISLIKE)
    private AbstractComment likeDislikeComment;

    public CommentLikeDislike() {
    }

    public CommentLikeDislike(String name) {
        super(name);
    }

    public CommentLikeDislike(String name, String description) {
        super(name, description);
    }

    public AbstractComment getLikeDislikeComment() {
        return likeDislikeComment;
    }

    public void setLikeDislikeComment(AbstractComment likeDislikeComment) {
        this.likeDislikeComment = likeDislikeComment;
    }

    @Override
    public String toString() {
        return "CommentLikeDislike{" +
                "likeDislikeComment=" + likeDislikeComment +
                '}';
    }
}
