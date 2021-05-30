package com.bgg.service;

import com.bgg.entity.Comment;
import com.bgg.entity.CommentOpr;
import com.bgg.entity.LatestComment;
import com.bgg.entity.Reply;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentsByBlogId(Long id);

    List<Reply> findRepliesByCommentId(Long id);

    void addComment(CommentOpr commentOpr);

    void addReply(Reply reply);

    List<LatestComment> findLatestComments();

    void deleteComment(Long id);

    void deleteReply(Long id);
}
