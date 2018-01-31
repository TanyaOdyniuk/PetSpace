package com.netcracker.service.comment.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.album.PhotoAlbumConstant;
import com.netcracker.model.comment.*;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.service.comment.CommentService;
import com.netcracker.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private StatusService statusService;

    @Override
    public List<WallRecordComment> getWallRecordComments(BigInteger recordID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + recordID + " AND ATTRTYPE_ID = " + RecordConstant.REC_COMREF +
                " and object_id in (select object_id from objreference where attrtype_id =" + CommentConstant.COM_AUTOR +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF + " ) "+
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, WallRecordComment.class, new QueryDescriptor());
    }

    @Override
    public List<GroupRecordComment> getGroupRecordComments(BigInteger recordID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + recordID + " AND ATTRTYPE_ID = " + RecordConstant.REC_COMREF +
                " and object_id in (select object_id from objreference where attrtype_id =" + CommentConstant.COM_AUTOR +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF + " ) "+
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, GroupRecordComment.class, new QueryDescriptor());
    }

    @Override
    public List<PhotoRecordComment> getPhotoRecordComments(BigInteger recordID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + recordID + " AND ATTRTYPE_ID = " + RecordConstant.PR_COMMENTS +
                " and object_id in (select object_id from objreference where attrtype_id =" + CommentConstant.COM_AUTOR +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF + " ) "+
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, PhotoRecordComment.class, new QueryDescriptor());
    }

    @Override
    public List<AdvertisementComment> getAdvertisementComments(BigInteger recordID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + recordID + " AND ATTRTYPE_ID = " + AdvertisementConstant.AD_COM +
                " and object_id in (select object_id from objreference where attrtype_id =" + CommentConstant.COM_AUTOR +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF + " ) "+
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addSortingDesc(CommentConstant.COM_DATE, "DESC", true);
        return entityManagerService.getObjectsBySQL(sqlQuery, AdvertisementComment.class, queryDescriptor);
    }

    @Override
    public Profile getCommentAuthor(BigInteger commentID) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + commentID + " AND ATTRTYPE_ID = " + CommentConstant.COM_AUTOR +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        List<Profile> commentAuthor = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return commentAuthor.get(0);
    }

    @Override
    public WallRecordComment createComment(WallRecordComment comment) {
        comment.setCommentState(statusService.getActiveStatus());
        return entityManagerService.create(comment);
    }

    @Override
    public GroupRecordComment createComment(GroupRecordComment comment) {
        comment.setCommentState(statusService.getActiveStatus());
        return entityManagerService.create(comment);
    }

    @Override
    public PhotoRecordComment createComment(PhotoRecordComment comment) {
        comment.setCommentState(statusService.getActiveStatus());
        return entityManagerService.create(comment);
    }

    @Override
    public AdvertisementComment createComment(AdvertisementComment comment) {
        comment.setCommentState(statusService.getActiveStatus());
        return entityManagerService.create(comment);
    }

    @Override
    public void updateComment(AbstractComment comment) {
        entityManagerService.update(comment);
    }

    @Override
    public void updateComment(WallRecordComment comment) {
        entityManagerService.update(comment);
    }

    @Override
    public void updateComment(GroupRecordComment comment) {
        entityManagerService.update(comment);
    }

    @Override
    public void updateComment(PhotoRecordComment comment) {
        entityManagerService.update(comment);
    }

    @Override
    public void updateComment(AdvertisementComment comment) {
        entityManagerService.update(comment);
    }

    @Override
    public void deleteRecordComment(AbstractComment comment) {
        entityManagerService.delete(comment.getObjectId(), -1);
    }
}
