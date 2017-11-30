package com.netcracker.service.newsfeed;

import com.netcracker.model.pet.Pet;

import java.util.List;

public interface NewsFeedService {
//    void showFeed(NewsFeed newsfeed);
//
//    void removeBoringContent(NewsFeed newsfeed);

    List<Pet> showTopPets();
}
