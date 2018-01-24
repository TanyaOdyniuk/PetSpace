package com.netcracker.service.search;

import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {
    List<Profile> searchForPeopleByFullName(String name, String surname);

    List<Profile> searchForPeopleByEmail(String email);

    List<Profile> searchPeopleByNameOrSurname(String name);

    List<Profile> searchForFriendsByFullName(String name, String surname, List<Profile> friendList);

    List<Profile> searchForFriendsByEmail(String email, List<Profile> friendList);

    List<Profile> searchFriendsByNameOrSurname(String name, List<Profile> friendList);
}
