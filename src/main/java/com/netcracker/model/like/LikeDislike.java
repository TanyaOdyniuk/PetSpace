package com.netcracker.model.like;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.comment.Comment;
import com.netcracker.model.record.GroupWallRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;

import java.util.Date;

@ObjectType(value = 400)
public class LikeDislike extends BaseEntity {
    @Attribute(value = 400)
    private Date likeDate;
    @Boolean(value = 401, yesno = "yes")
    private boolean isDislike;
    @Attribute(value = 402)
    private Profile likeAuthor;
    @Reference(value = 409)
    private GroupWallRecord likeGroupWallRecord;
    @Reference(value = 405)
    private Comment likeComment;
    @Reference(value = 305)
    private PhotoRecord likePhotoRecord;

    public LikeDislike() {
    }

    public LikeDislike(String name) {
        super(name);
    }

    public LikeDislike(String name, String description) {
        super(name, description);
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }

    public boolean getDislike() {
        return isDislike;
    }

    public void setDislike(boolean dislike) {
        isDislike = dislike;
    }

    public Profile getLikeAuthor() {
        return likeAuthor;
    }

    public void setLikeAuthor(Profile likeAuthor) {
        this.likeAuthor = likeAuthor;
    }

    public GroupWallRecord getLikeGroupWallRecord() {
        return likeGroupWallRecord;
    }

    public void setLikeGroupWallRecord(GroupWallRecord likeGroupWallRecord) {
        this.likeGroupWallRecord = likeGroupWallRecord;
    }

    public Comment getLikeComment() {
        return likeComment;
    }

    public void setLikeComment(Comment likeComment) {
        this.likeComment = likeComment;
    }

    public PhotoRecord getLikePhotoRecord() {
        return likePhotoRecord;
    }

    public void setLikePhotoRecord(PhotoRecord likePhotoRecord) {
        this.likePhotoRecord = likePhotoRecord;
    }

    @Override
    public String toString() {
        return "LikeDislike{" +
                "likeDate=" + likeDate +
                ", isDislike=" + isDislike +
                ", likeAuthor=" + likeAuthor +
                ", likeGroupWallRecord=" + likeGroupWallRecord +
                ", likeComment=" + likeComment +
                ", likePhotoRecord=" + likePhotoRecord +
                '}';
    }
}
