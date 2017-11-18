package com.netcracker.model.like;

import com.netcracker.model.Model;
import com.netcracker.model.comment.Comment;
import com.netcracker.model.record.GroupWallRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;

import java.util.Date;

public class LikeDislike extends Model {
    private Date likeDate;
    private Boolean isDislike;
    private Profile likeProfile;
    private GroupWallRecord likeGroupWallRecord;
    private Comment likeComment;
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

    public Boolean getDislike() {
        return isDislike;
    }

    public void setDislike(Boolean dislike) {
        isDislike = dislike;
    }

    public Profile getLikeProfile() {
        return likeProfile;
    }

    public void setLikeProfile(Profile likeProfile) {
        this.likeProfile = likeProfile;
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
                ", likeProfile=" + likeProfile +
                ", likeGroupWallRecord=" + likeGroupWallRecord +
                ", likeComment=" + likeComment +
                ", likePhotoRecord=" + likePhotoRecord +
                '}';
    }
}
