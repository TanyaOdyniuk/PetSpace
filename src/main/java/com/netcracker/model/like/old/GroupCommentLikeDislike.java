package com.netcracker.model.like.old;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.old.GroupComment;

@ObjectType(value = 400)
public class GroupCommentLikeDislike extends AbstractLikeDislike {
    @Reference(value = 405)
    private GroupComment groupComment;

    public GroupCommentLikeDislike() {
    }

    public GroupCommentLikeDislike(String name) {
        super(name);
    }

    public GroupCommentLikeDislike(String name, String description) {
        super(name, description);
    }

    public GroupComment getGroupComment() {
        return groupComment;
    }

    public void setGroupComment(GroupComment groupComment) {
        this.groupComment = groupComment;
    }

    @Override
    public String toString() {
        return "GroupCommentLikeDislike{" +
                "groupComment=" + groupComment +
                '}';
    }
}
