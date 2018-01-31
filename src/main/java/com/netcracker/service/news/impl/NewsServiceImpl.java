package com.netcracker.service.news.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupConstant;
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

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS;
import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

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
        String getQuery = "select OBJECT_ID FROM OBJREFERENCE " +
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
                "                      AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO + ")" +
                                     " and " + IGNORING_DELETED_ELEMENTS_IN_REF +
                "                    )" + " and " + IGNORING_DELETED_ELEMENTS +
                "        )" +
                "  " +" and " + IGNORING_DELETED_ELEMENTS_IN_REF +
                "  AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO + ")" +
                "  AND REFERENCE <> " + profileId + " and " + IGNORING_DELETED_ELEMENTS_IN_REF +
                ")";
        return pageCounterService.getPageCount(newsFriendsCapacity, entityManagerService.getBySqlCount(getQuery));
    }

    public int getNewsGroupsPageCount(BigInteger profileId) {
        Integer newsGroupsCapacity = new Integer(newsGroupsCapacityProp);
        String getQuery = "select OBJECT_ID FROM OBJREFERENCE " +
                "where ATTRTYPE_ID = " + GroupConstant.GR_RECORDS +
                " and REFERENCE IN ( " +
                "SELECT distinct object_id FROM OBJREFERENCE" +
                "  WHERE reference = " + profileId +
                "  and ATTRTYPE_ID in (" + GroupConstant.GR_ADMIN + ", " + GroupConstant.GR_PARTICIPANTS +")" +
                "  and " + IGNORING_DELETED_ELEMENTS_IN_REF + ")" +
                " and OBJECT_ID in (select OBJECT_ID FROM OBJREFERENCE " +
        "                   WHERE ATTRTYPE_ID =" + GroupConstant.GR_AUTOR +" and "  + IGNORING_DELETED_ELEMENTS_IN_REF + ")" +
                "  and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return pageCounterService.getPageCount(newsGroupsCapacity, entityManagerService.getBySqlCount(getQuery));
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
                "                      AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO + ")" +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF +
                "                    )" + " and " + IGNORING_DELETED_ELEMENTS +
                "        )" +
                "  " +" and " + IGNORING_DELETED_ELEMENTS_IN_REF +
                "  AND ATTRTYPE_ID IN (" + FriendRequestConstant.REQ_FROM + ", " + FriendRequestConstant.REQ_TO + ")" +
                "  AND REFERENCE <> " + profileId + " and " + IGNORING_DELETED_ELEMENTS_IN_REF +
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
    public List<GroupRecord> getGroupsWallRecords(BigInteger profileId, Integer pageNumber) {
        String selectGroupsRecord = "select OBJECT_ID FROM OBJREFERENCE " +
                "where ATTRTYPE_ID = " + GroupConstant.GR_RECORDS +
                " and REFERENCE IN ( " +
                "SELECT distinct object_id FROM OBJREFERENCE" +
                "  WHERE reference = " + profileId +
                "  and ATTRTYPE_ID in (" + GroupConstant.GR_ADMIN + ", " + GroupConstant.GR_PARTICIPANTS +")" +
                "  and " + IGNORING_DELETED_ELEMENTS_IN_REF + ")" +
                " and OBJECT_ID in (select OBJECT_ID FROM OBJREFERENCE " +
                "                   WHERE ATTRTYPE_ID =" + GroupConstant.GR_AUTOR +" and "  + IGNORING_DELETED_ELEMENTS_IN_REF + ")" +
                "  and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, new Integer(newsGroupsCapacityProp));
        queryDescriptor.addSortingDesc(RecordConstant.REC_DATE, "DESC", true);
        List<GroupRecord> groupsRecords = entityManagerService.getObjectsBySQL(selectGroupsRecord, GroupRecord.class, queryDescriptor);
        for (GroupRecord record : groupsRecords) {
            BigInteger wallRecordId = record.getObjectId();
            record.setRecordAuthor(recordService.getRecordAuthor(wallRecordId));
            record.setParentGroup(recordService.getGroupRecordOwner(wallRecordId));
        }
        return groupsRecords;
    }
}
