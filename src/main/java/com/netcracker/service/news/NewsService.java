package com.netcracker.service.news;

import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;

import java.math.BigInteger;
import java.util.List;

public interface NewsService {
    int getNewsFriendsPageCount(BigInteger profileId);
    int getNewsGroupsPageCount(BigInteger profileId);

    List<WallRecord> getFriendsWallRecords(BigInteger profileId, Integer pageNumber);
    List<GroupRecord> getGroupsWallRecords(BigInteger profileId, Integer pageNumber);
}
