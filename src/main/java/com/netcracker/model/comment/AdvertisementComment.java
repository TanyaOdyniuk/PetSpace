package com.netcracker.model.comment;

import com.netcracker.model.advertisement.Advertisement;

public class AdvertisementComment extends AbstractComment {
    private Advertisement commentAdvertisement;

    public AdvertisementComment() {
    }

    public AdvertisementComment(String name) {
        super(name);
    }

    public AdvertisementComment(String name, String description) {
        super(name, description);
    }

    public Advertisement getCommentAdvertisement() {
        return commentAdvertisement;
    }

    public void setCommentAdvertisement(Advertisement commentAdvertisement) {
        this.commentAdvertisement = commentAdvertisement;
    }

    @Override
    public String toString() {
        return "AdvertisementComment{" +
                "commentAdvertisement=" + commentAdvertisement +
                '}';
    }
}
