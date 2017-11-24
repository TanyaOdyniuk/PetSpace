package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.Comment;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.LikeDislike;
import com.netcracker.model.user.Profile;

import java.util.List;

@ObjectType(value = 402)
public class GroupWallRecord extends AbstractRecord {

    @Reference(value = 428)
    private Group recordGroup;
    @Attribute(value = 411)
    private Profile recordAuthor;
    //TODO SERVICE TO GET LIKES
    @Attribute(value = 409)
    private List<LikeDislike> recordLikes;
    //TODO SERVICE TO GET DISLIKES
    @Attribute(value = 409)
    private List<LikeDislike> recordDislikes;
    //TODO SERVICE TO GET COMMENTS
    @Reference(value = 410)
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

    public Profile getRecordAuthor() {
        return recordAuthor;
    }

    public void setRecordAuthor(Profile recordAuthor) {
        this.recordAuthor = recordAuthor;
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
                ", recordAuthor=" + recordAuthor +
                ", recordLikes=" + recordLikes +
                ", recordDislikes=" + recordDislikes +
                ", GroupWallComments=" + GroupWallComments +
                '}';
    }
}
