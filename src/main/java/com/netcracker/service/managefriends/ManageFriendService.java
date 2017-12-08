package com.netcracker.service.managefriends;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public interface ManageFriendService {
    void addFriend(Profile profile);

    void deleteFriend(Profile profile);

    Pet checkFriendsPets(Profile profile);

    void sortFriendList(SortListFriendMod sortMod);

    interface SortListFriendMod {
        String UPLOAD_DATE = "UPLOAD_DATE";
        String POPULARITY_AMOUNT_OF_LIKES_PET = "POPULARITY_AMOUNT_OF_LIKES_PET";
        String ALPHABETICALLY = "ALPHABETICALLY";
    }

    List<Profile> getFriendList(BigInteger profileId, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc);
}