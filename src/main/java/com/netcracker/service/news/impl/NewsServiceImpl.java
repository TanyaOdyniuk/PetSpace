package com.netcracker.service.news.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.request.FriendRequestConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.service.news.NewsService;
import com.netcracker.service.record.RecordService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private PageCounterService pageCounterService;
    @Value("${news.friends.pageCapacity}")
    private String newsFriendsCapacityProp;
    @Value("${news.groups.pageCapacity}")
    private String newsGroupsCapacityProp;

    public int getNewsFriendsPageCount(BigInteger profileId) {
        Integer newsFriendsCapacity = new Integer(newsFriendsCapacityProp);
        String getAdsQuery = "select OBJECT_ID FROM OBJREFERENCE " +
                "where ATTRTYPE_ID = " + RecordConstant.REC_WALLOWNER +
                " and REFERENCE IN ( " +
                "SELECT REFERENCE FROM OBJREFERENCE" +
                "  WHERE OBJECT_ID IN" +
                "   ( SELECT OBJECT_ID FROM OBJREFERENCE " +
                "          WHERE REFERENCE = 5 " +
                "                AND OBJECT_ID IN" +
                "                    (" +
                "                      SELECT OBJECT_ID FROM OBJREFERENCE" +
                "                      WHERE REFERENCE = " + profileId +
                "                            AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO +")" +
                "                    )" +
                "        )" +
                "  AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO +")" +
                "  AND REFERENCE <> " + profileId +
                ")";
        return pageCounterService.getPageCount(newsFriendsCapacity, entityManagerService.getBySqlCount(getAdsQuery));
    }

    @Override
    public List<WallRecord> getFriendsWallRecords(BigInteger profileId, Integer pageNumber) {
        String selectFriendsRecord = "select OBJECT_ID FROM OBJREFERENCE " +
                "where ATTRTYPE_ID = " + RecordConstant.REC_WALLOWNER +
                " and REFERENCE IN ( " +
                "SELECT REFERENCE FROM OBJREFERENCE" +
                "  WHERE OBJECT_ID IN" +
                "   ( SELECT OBJECT_ID FROM OBJREFERENCE " +
                "          WHERE REFERENCE = 5 " +
                "                AND OBJECT_ID IN" +
                "                    (" +
                "                      SELECT OBJECT_ID FROM OBJREFERENCE" +
                "                      WHERE REFERENCE = " + profileId +
                "                            AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO +")" +
                "                    )" +
                "        )" +
                "  AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO +")" +
                "  AND REFERENCE <> " + profileId +
                ")";
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, new Integer(newsFriendsCapacityProp));
        queryDescriptor.addSortingDesc(RecordConstant.REC_DATE, "DESC", true);
        List<WallRecord> friendsRecords = entityManagerService.getObjectsBySQL(selectFriendsRecord, WallRecord.class, queryDescriptor);
        for (WallRecord record : friendsRecords) {
            BigInteger wallRecordId = record.getObjectId();
            record.setRecordAuthor(recordService.getRecordAuthor(wallRecordId));
            record.setWallOwner(recordService.getWallRecordOwner(wallRecordId));
        }
        return friendsRecords;
    }

    @Override
    public List<GroupRecord> getGroupsWallRecords(BigInteger profileId) {
        return null;
    }
}
