package com.netcracker.model.record.old;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.old.WallComment;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.user.Profile;

import java.util.List;

@ObjectType(value = 402)
public class WallRecord extends AbstractRecord {

    @Reference(value = 421)
    private Profile profile;
    @Attribute(value = 410)
    private List<WallComment> wallComments;

    public WallRecord() {
    }

    public WallRecord(String name) {
        super(name);
    }

    public WallRecord(String name, String description) {
        super(name, description);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<WallComment> getWallComments() {
        return wallComments;
    }

    public void setWallComments(List<WallComment> wallComments) {
        this.wallComments = wallComments;
    }

    @Override
    public String toString() {
        return "WallRecord{" +
                "profile=" + profile +
                ", wallComments=" + wallComments +
                '}';
    }
}
