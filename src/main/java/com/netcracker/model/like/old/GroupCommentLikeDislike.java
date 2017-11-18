package com.netcracker.model.like.old;

import com.netcracker.model.comment.old.GroupComment;

public class GroupCommentLikeDislike extends AbstractLikeDislike {
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
