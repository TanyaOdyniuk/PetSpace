package com.netcracker.controller.news;

import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.service.news.NewsService;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @GetMapping("/friends/{profileId}/{pageNumber}")
    public RestResponsePage<WallRecord> getFriendsWallRecords(@PathVariable("profileId") BigInteger profileId, @PathVariable("pageNumber") Integer pageNumber){
        Integer count = newsService.getNewsFriendsPageCount(profileId);
        return new RestResponsePage<>(newsService.getFriendsWallRecords(profileId, pageNumber), null, count);
    }

    @GetMapping("/groups/{profileId}/{pageNumber}")
    public RestResponsePage<GroupRecord> getGroupsWallRecords(@PathVariable("profileId") BigInteger profileId, @PathVariable("pageNumber") Integer pageNumber){
        Integer count = newsService.getNewsGroupsPageCount(profileId);
        return new RestResponsePage<>(newsService.getGroupsWallRecords(profileId, pageNumber), null, count);
    }
}
