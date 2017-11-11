package com.netcracker.model.record;

import com.netcracker.model.comment.WallComment;
import com.netcracker.model.user.Profile;

import java.util.List;

public class WallRecord extends AbstractRecord {

    private Profile profile;
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
