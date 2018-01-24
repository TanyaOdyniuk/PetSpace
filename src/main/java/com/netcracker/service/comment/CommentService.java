package com.netcracker.service.comment;

import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface CommentService {

    //Get list of all comments on wall record by its ID
    List<WallRecordComment> getWallRecordComments(BigInteger wallRecordID);

    //Get author`s profile from certain wall record comment
    Profile getCommentAuthor(BigInteger commentID);

    //Create new wall record comment (with already filled fields)
    WallRecordComment createWallRecordComment(WallRecordComment comment);

    //Update existing wall record comment (with already filled fields)
    void updateWallRecordComment(WallRecordComment comment);

    //Delete wall record comment
    void deleteWallRecordComment(WallRecordComment comment);
}
