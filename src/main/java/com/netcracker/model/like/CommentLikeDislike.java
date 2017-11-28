package com.netcracker.model.like;

import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.AbstractComment;

public class CommentLikeDislike extends AbstractLikeDislike {
    @Reference(value = 405)
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
