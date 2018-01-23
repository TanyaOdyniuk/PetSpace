package com.netcracker.controller.like;

import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.RecordLike;
import com.netcracker.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    LikeService likeService;

    @GetMapping("/record/{id}")
    public List<RecordLike> getRecordLikes(@PathVariable("id") BigInteger wallRecordID) {
        return likeService.getRecordLikes(wallRecordID);
    }

    @GetMapping("/comment/{id}")
    public List<CommentLike> getCommentLikes(@PathVariable("id") BigInteger commentID) {
        return likeService.getCommentLikes(commentID);
    }

    @PostMapping("/record/add")
    public RecordLike createRecordLike(@RequestBody RecordLike recordLike) {
        return likeService.createRecordLike(recordLike);
    }

    @PostMapping("/comment/add")
    public CommentLike createCommentLike(@RequestBody CommentLike commentLike) {
        return likeService.createCommentLike(commentLike);
    }
    @GetMapping("/delete/{adId}")
    public void deleteLike(@PathVariable("adId") BigInteger likeID){
        likeService.deleteLike(likeID);
    }
}
