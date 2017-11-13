package com.netcracker.model.comment;

import com.netcracker.model.advertisement.Advertisement;

public class AdvertisementComment extends AbstractComment {
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
