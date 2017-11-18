package com.netcracker.model.record;

import com.netcracker.model.comment.Comment;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.LikeDislike;
import com.netcracker.model.user.Profile;

import java.util.List;

public class GroupWallRecord extends AbstractRecord {
    private Group recordGroup;
    private Profile recordProfile;
    //TODO SERVICE TO GET LIKES
    private List<LikeDislike> recordLikes;
    //TODO SERVICE TO GET DISLIKES
    private List<LikeDislike> recordDislikes;
    //TODO SERVICE TO GET COMMENTS
    private List<Comment> GroupWallComments;

    public GroupWallRecord() {
    }

    public GroupWallRecord(String name) {
        super(name);
    }

    public GroupWallRecord(String name, String description) {
        super(name, description);
    }

    public Group getRecordGroup() {
        return recordGroup;
    }

    public void setRecordGroup(Group recordGroup) {
        this.recordGroup = recordGroup;
    }

    public Profile getRecordProfile() {
        return recordProfile;
    }

    public void setRecordProfile(Profile recordProfile) {
        this.recordProfile = recordProfile;
    }

    public List<LikeDislike> getRecordLikes() {
        return recordLikes;
    }

    public void setRecordLikes(List<LikeDislike> recordLikes) {
        this.recordLikes = recordLikes;
    }

    public List<LikeDislike> getRecordDislikes() {
        return recordDislikes;
    }

    public void setRecordDislikes(List<LikeDislike> recordDislikes) {
        this.recordDislikes = recordDislikes;
    }

    public List<Comment> getGroupWallComments() {
        return GroupWallComments;
    }

    public void setGroupWallComments(List<Comment> groupWallComments) {
        GroupWallComments = groupWallComments;
    }

    @Override
    public String toString() {
        return "GroupWallRecord{" +
                "recordGroup=" + recordGroup +
                ", recordProfile=" + recordProfile +
                ", recordLikes=" + recordLikes +
                ", recordDislikes=" + recordDislikes +
                ", GroupWallComments=" + GroupWallComments +
                '}';
    }
}
