package com.netcracker.service.comment;

import com.netcracker.model.comment.*;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface CommentService {

    //Get list of all comments on record by its ID
    List<WallRecordComment> getWallRecordComments(BigInteger recordID);

    //Get list of all comments on record by its ID
    List<GroupRecordComment> getGroupRecordComments(BigInteger recordID);

    //Get list of all comments on record by its ID
    List<PhotoRecordComment> getPhotoRecordComments(BigInteger recordID);

    //Get list of all comments on record by its ID
    List<AdvertisementComment> getAdvertisementComments(BigInteger recordID);

    //Get author`s profile from certain wall record comment
    Profile getCommentAuthor(BigInteger commentID);

    //Create new wall record comment (with already filled fields)
    WallRecordComment createComment(WallRecordComment comment);

    //Create new group record comment (with already filled fields)
    GroupRecordComment createComment(GroupRecordComment comment);

    //Create new photo record comment (with already filled fields)
    PhotoRecordComment createComment(PhotoRecordComment comment);

    //Create new advertisement comment (with already filled fields)
    AdvertisementComment createComment(AdvertisementComment comment);

    //Update existing wall record comment (with already filled fields)
    void updateComment(AbstractComment comment);

    //Update existing wall record comment (with already filled fields)
    void updateComment(WallRecordComment comment);

    //Update existing group record comment (with already filled fields)
    void updateComment(GroupRecordComment comment);

    //Update existing photo record comment (with already filled fields)
    void updateComment(PhotoRecordComment comment);

    //Update existing advertisement comment (with already filled fields)
    void updateComment(AdvertisementComment comment);

    //Delete record comment
    void deleteRecordComment(AbstractComment comment);
}
