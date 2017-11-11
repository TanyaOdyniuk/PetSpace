package com.netcracker.model.album;

import com.netcracker.model.Model;
import com.netcracker.model.record.AvatarRecord;
import com.netcracker.model.user.Profile;

import java.util.List;

public class AvatarAlbum extends Model {

    private Profile profile;
    private List<AvatarRecord> avatarRecords;

    public AvatarAlbum() {
    }

    public AvatarAlbum(String name) {
        super(name);
    }

    public AvatarAlbum(String name, String description) {
        super(name, description);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<AvatarRecord> getAvatarRecords() {
        return avatarRecords;
    }

    public void setAvatarRecords(List<AvatarRecord> avatarRecords) {
        this.avatarRecords = avatarRecords;
    }

    @Override
    public String toString() {
        return "AvatarAlbum{" +
                "profile=" + profile +
                ", avatarRecords=" + avatarRecords +
                '}';
    }
}
