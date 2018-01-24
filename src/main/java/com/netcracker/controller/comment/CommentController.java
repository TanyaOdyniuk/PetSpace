package com.netcracker.controller.comment;

import com.netcracker.model.comment.*;
import com.netcracker.model.user.Profile;
import com.netcracker.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/wall/{id}")
    public List<WallRecordComment> getComments(@PathVariable("id") BigInteger recordID) {
        return commentService.getWallRecordComments(recordID);
    }
    @GetMapping("/ad/{id}")
    public List<AdvertisementComment> getAdvertisementComments(@PathVariable("id") BigInteger recordID) {
        return commentService.getAdvertisementComments(recordID);
    }
    @GetMapping("/author/{id}")
    public Profile getCommentAuthor(@PathVariable("id") BigInteger commentID) {
        return commentService.getCommentAuthor(commentID);
    }

    @PostMapping("/wall/add")
    public WallRecordComment createComment(@RequestBody WallRecordComment comment) {
        return commentService.createComment(comment);
    }

    @PostMapping("/group/add")
    public GroupRecordComment createComment(@RequestBody GroupRecordComment comment) {
        return commentService.createComment(comment);
    }

    @PostMapping("/photo/add")
    public PhotoRecordComment createComment(@RequestBody PhotoRecordComment comment) {
        return commentService.createComment(comment);
    }

    @PostMapping("/ad/add")
    public AdvertisementComment createComment(@RequestBody AdvertisementComment comment) {
        return commentService.createComment(comment);
    }

    @PostMapping("/wall/update")
    public void updateComment(@RequestBody WallRecordComment comment){
        commentService.updateComment(comment);
    }

    @PostMapping("/update")
    public void updateComment(@RequestBody AbstractComment comment){
        commentService.updateComment(comment);
    }

    @PostMapping("/group/update")
    public void updateComment(@RequestBody GroupRecordComment comment){
        commentService.updateComment(comment);
    }

    @PostMapping("/photo/update")
    public void updateComment(@RequestBody PhotoRecordComment comment){
        commentService.updateComment(comment);
    }

    @PostMapping("/ad/update")
    public void updateComment(@RequestBody AdvertisementComment comment){
        commentService.updateComment(comment);
    }

    @PostMapping("/delete")
    public void deleteRecordComment(@RequestBody AbstractComment comment){
        commentService.deleteRecordComment(comment);
    }
}
