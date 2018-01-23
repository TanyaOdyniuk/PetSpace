package com.netcracker.service.like;

import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.RecordLike;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface LikeService {

    //Create like/dislike for the record
    RecordLike createRecordLike(RecordLike likeDislike);

    //Create like/dislike for the comment
    CommentLike createCommentLike(CommentLike commentLike);

    //Get all likes/dislikes for this record
    List<RecordLike> getRecordLikes(BigInteger wallRecordID);

    //Get all likes/dislikes for this comment
    List<CommentLike> getCommentLikes(BigInteger commentID);

    //Delete like/dislike
    void deleteLike(BigInteger likeID);

}
