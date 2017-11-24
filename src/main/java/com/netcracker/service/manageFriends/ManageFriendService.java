package com.netcracker.service.manageFriends;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;

public interface ManageFriendService {
    void addFriend(Profile profile);

    void deleteFriend(Profile profile);

    Pet checkFriendsPets(Profile profile);

    void SortFriendList(SortListFriendMod sortMod);

    interface SortListFriendMod {
        String UPLOAD_DATE = "UPLOAD_DATE";
        String POPULARITY_AMOUNT_OF_LIKES_PET = "POPULARITY_AMOUNT_OF_LIKES_PET";
        String ALPHABETICALLY = "ALPHABETICALLY";
    }
}
