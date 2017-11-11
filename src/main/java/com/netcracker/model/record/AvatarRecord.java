package com.netcracker.model.record;

import com.netcracker.model.album.AvatarAlbum;
import com.netcracker.model.user.Profile;

public class AvatarRecord extends AbstractRecord {

    private Object photo;
    private AvatarAlbum avatarAlbum;
    private Profile profile;

    public AvatarRecord() {
    }

    public AvatarRecord(String name) {
        super(name);
    }

    public AvatarRecord(String name, String description) {
        super(name, description);
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public AvatarAlbum getAvatarAlbum() {
        return avatarAlbum;
    }

    public void setAvatarAlbum(AvatarAlbum avatarAlbum) {
        this.avatarAlbum = avatarAlbum;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "AvatarRecord{" +
                "photo=" + photo +
                ", avatarAlbum=" + avatarAlbum +
                ", profile=" + profile +
                '}';
    }
}
