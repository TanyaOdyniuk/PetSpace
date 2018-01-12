package com.netcracker.service.comment.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.comment.CommentConstant;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.service.comment.CommentService;
import com.netcracker.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    EntityManagerService entityManagerService;
    @Autowired
    private StatusService statusService;

    @Override
    public List<WallRecordComment> getWallRecordComments(BigInteger wallRecordID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + wallRecordID + " AND ATTRTYPE_ID = " + RecordConstant.REC_COMREF;
        return entityManagerService.getObjectsBySQL(sqlQuery, WallRecordComment.class, new QueryDescriptor());
    }

    @Override
    public WallRecordComment createWallRecordComment(WallRecordComment comment) {
        return entityManagerService.create(comment);
    }

    @Override
    public WallRecordComment updateWallRecordComment(WallRecordComment comment) {
        return null;
    }

    @Override
    public void deleteWallRecordComment(WallRecordComment comment) {

    }

    @Override
    public Profile getCommentAuthor(BigInteger commentID) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + commentID + " AND ATTRTYPE_ID = " + CommentConstant.COM_AUTOR;
        List<Profile> commentAuthor = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return commentAuthor.get(0);
    }
}
