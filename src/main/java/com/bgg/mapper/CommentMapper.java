package com.bgg.mapper;

import com.bgg.entity.Comment;
import com.bgg.entity.CommentOpr;
import com.bgg.entity.LatestComment;
import com.bgg.entity.Reply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> findCommentsByBlogId(Long id);

    List<Reply> findRepliesByCommentId(Long id);

    void addComment(CommentOpr commentOpr);

    void addReply(Reply reply);

    List<LatestComment> findLatestComments();

    void deleteComment(Long id);

    void deleteReply(Long id);

    List<Long> findReplyIdByCommentId(Long id);
}
