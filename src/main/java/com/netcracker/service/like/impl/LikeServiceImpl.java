package com.netcracker.service.like.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.comment.CommentConstant;
import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.LikeConstant;
import com.netcracker.model.like.RecordLike;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public RecordLike createRecordLike(RecordLike like) {
        return entityManagerService.create(like);
    }

    @Override
    public CommentLike createCommentLike(CommentLike like) {
        return entityManagerService.create(like);
    }

    @Override
    public List<RecordLike> getRecordLikes(BigInteger wallRecordID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + wallRecordID + " AND ATTRTYPE_ID = " + RecordConstant.REC_LDLREF;
        return entityManagerService.getObjectsBySQL(sqlQuery, RecordLike.class, new QueryDescriptor());
    }

    @Override
    public List<CommentLike> getCommentLikes(BigInteger commentID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + commentID + " AND ATTRTYPE_ID = " + CommentConstant.COM_LIKEDISLIKE;
        return entityManagerService.getObjectsBySQL(sqlQuery, CommentLike.class, new QueryDescriptor());
    }

    @Override
    public void deleteLike(BigInteger likeID) {
        entityManagerService.delete(likeID, 0);
    }
}
