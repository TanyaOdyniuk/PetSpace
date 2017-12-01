package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;

@ObjectType(CommentConstant.COM_TYPE)
public class AdvertisementComment extends AbstractComment {
    @Reference(AdvertisementConstant.AD_TYPE)//нет списка комментов
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
