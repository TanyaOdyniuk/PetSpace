package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;

@ObjectType(CommentConstant.COM_TYPE)
public class AdvertisementComment extends AbstractComment {
    @Reference(value = AdvertisementConstant.AD_TYPE, isParentChild = 0)
    private Advertisement commentedAdvertisement;

    public AdvertisementComment() {
    }

    public AdvertisementComment(String name) {
        super(name);
    }

    public AdvertisementComment(String name, String description) {
        super(name, description);
    }

    public Advertisement getCommentedAdvertisement() {
        return commentedAdvertisement;
    }

    public void setCommentedAdvertisement(Advertisement commentedAdvertisement) {
        this.commentedAdvertisement = commentedAdvertisement;
    }

    @Override
    public String toString() {
        return "AdvertisementComment{" +
                "commentedAdvertisement=" + commentedAdvertisement +
                '}';
    }
}
