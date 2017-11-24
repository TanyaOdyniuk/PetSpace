package com.netcracker.model.comment.old;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.advertisement.Advertisement;

@ObjectType(value = 401)
public class AdvertisementComment extends AbstractComment {
    //@Reference(value = ) в advertisements нет связи с комментариями
    private Advertisement advertisement;

    public AdvertisementComment() {
    }

    public AdvertisementComment(String name) {
        super(name);
    }

    public AdvertisementComment(String name, String description) {
        super(name, description);
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    @Override
    public String toString() {
        return "AdvertisementComment{" +
                "advertisement=" + advertisement +
                '}';
    }
}
