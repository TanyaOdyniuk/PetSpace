package com.netcracker.model.like.old;

import com.netcracker.model.comment.old.PhotoComment;

public class PhotoCommentLikeDislike extends AbstractLikeDislike {
    private PhotoComment photoComment;

    public PhotoCommentLikeDislike() {
    }

    public PhotoCommentLikeDislike(String name) {
        super(name);
    }

    public PhotoCommentLikeDislike(String name, String description) {
        super(name, description);
    }

    public PhotoComment getPhotoComment() {
        return photoComment;
    }

    public void setPhotoComment(PhotoComment photoComment) {
        this.photoComment = photoComment;
    }

    @Override
    public String toString() {
        return "PhotoCommentLikeDislike{" +
                "photoComment=" + photoComment +
                '}';
    }
}
