package com.netcracker.model.like;

import com.netcracker.model.comment.WallComment;

public class WallCommentLikeDislike extends AbstractLikeDislike {
    private WallComment wallComment;

    public WallCommentLikeDislike() {
    }

    public WallCommentLikeDislike(String name) {
        super(name);
    }

    public WallCommentLikeDislike(String name, String description) {
        super(name, description);
    }

    public WallComment getWallComment() {
        return wallComment;
    }

    public void setWallComment(WallComment wallComment) {
        this.wallComment = wallComment;
    }

    @Override
    public String toString() {
        return "WallCommentLikeDislike{" +
                "wallComment=" + wallComment +
                '}';
    }
}
