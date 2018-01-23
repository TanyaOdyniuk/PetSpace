package com.netcracker.controller.comment;

import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.record.WallRecord;
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
    CommentService commentService;

    @GetMapping("/{id}")
    public List<WallRecordComment> getWallRecordComments(@PathVariable("id") BigInteger wallRecordID) {
        return commentService.getWallRecordComments(wallRecordID);
    }

    @GetMapping("/author/{id}")
    public Profile getCommentAuthor(@PathVariable("id") BigInteger commentID) {
        return commentService.getCommentAuthor(commentID);
    }

    @PostMapping("/add")
    public WallRecordComment createWallRecordComment(@RequestBody WallRecordComment comment) {
        return commentService.createWallRecordComment(comment);
    }

    @PostMapping("/update")
    public void updateWallRecordComment(@RequestBody WallRecordComment comment){
        commentService.updateWallRecordComment(comment);
    }

    @PostMapping("/delete")
    public void deleteWallRecordComment(@RequestBody WallRecordComment comment){
        commentService.deleteWallRecordComment(comment);
    }
}
