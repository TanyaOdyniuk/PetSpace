package com.netcracker.service.comment;

import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface CommentService {

    //Get list of comment from certain wall record
    List<WallRecordComment> getWallRecordComments(BigInteger wallRecordID);

    //Add comment to the wall record
    WallRecordComment createWallRecordComment(WallRecordComment comment);

    //Edit comment on the wall record
    WallRecordComment updateWallRecordComment(WallRecordComment comment);

    //Delete comment from wall record
    void deleteWallRecordComment(WallRecordComment comment);

    //Get author`s profile of the current comment
    Profile getCommentAuthor(BigInteger commentID);

}
