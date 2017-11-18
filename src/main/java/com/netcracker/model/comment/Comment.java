package com.netcracker.model.comment;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.like.LikeDislike;
import com.netcracker.model.record.GroupWallRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;

import java.util.List;

public class Comment extends BaseEntity {
    private String commentText;
    private String commentDate;
    private Profile commentProfile;
    private Advertisement commentAdvertisement;
    private PhotoRecord commentPhotoRecord;
    private GroupWallRecord commentGroupWallRecord;
    //TODO SERVICE GET LIKES
    private List<LikeDislike> commentLikes;
    //TODO SERVICE GET DISLIKES
    private List<LikeDislike> commentDislikes;

    public Comment() {
    }

    public Comment(String name) {
        super(name);
    }

    public Comment(String name, String description) {
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

    public Profile getCommentProfile() {
        return commentProfile;
    }

    public void setCommentProfile(Profile commentProfile) {
        this.commentProfile = commentProfile;
    }

    public List<LikeDislike> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(List<LikeDislike> commentLikes) {
        this.commentLikes = commentLikes;
    }

    public List<LikeDislike> getCommentDislikes() {
        return commentDislikes;
    }

    public void setCommentDislikes(List<LikeDislike> commentDislikes) {
        this.commentDislikes = commentDislikes;
    }

    public Advertisement getCommentAdvertisement() {
        return commentAdvertisement;
    }

    public void setCommentAdvertisement(Advertisement commentAdvertisement) {
        this.commentAdvertisement = commentAdvertisement;
    }

    public PhotoRecord getCommentPhotoRecord() {
        return commentPhotoRecord;
    }

    public void setCommentPhotoRecord(PhotoRecord commentPhotoRecord) {
        this.commentPhotoRecord = commentPhotoRecord;
    }

    public GroupWallRecord getCommentGroupWallRecord() {
        return commentGroupWallRecord;
    }

    public void setCommentGroupWallRecord(GroupWallRecord commentGroupWallRecord) {
        this.commentGroupWallRecord = commentGroupWallRecord;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentText='" + commentText + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", commentProfile=" + commentProfile +
                ", commentLikes=" + commentLikes +
                ", commentDislikes=" + commentDislikes +
                ", commentAdvertisement=" + commentAdvertisement +
                ", commentPhotoRecord=" + commentPhotoRecord +
                ", commentGroupWallRecord=" + commentGroupWallRecord +
                '}';
    }
}
