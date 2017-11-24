package com.netcracker.model.like.old;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.old.PhotoComment;

@ObjectType(value = 400)
public class PhotoCommentLikeDislike extends AbstractLikeDislike {
    @Reference(value = 304)
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
