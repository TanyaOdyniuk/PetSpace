package com.netcracker.model.like;

import com.netcracker.model.Model;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.User;

public class Like extends Model {
    private PhotoRecord photoRecord;
    private User user;

    public Like() {
    }

    public Like(String name) {
        super(name);
    }

    public Like(String name, String description) {
        super(name, description);
    }

    public PhotoRecord getPhotoRecord() {
        return photoRecord;
    }

    public void setPhotoRecord(PhotoRecord photoRecord) {
        this.photoRecord = photoRecord;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Like{" +
                "photoRecord=" + photoRecord +
                ", user=" + user +
                '}';
    }
}
