package com.bgg.service.impl;

import com.bgg.entity.Comment;
import com.bgg.entity.CommentOpr;
import com.bgg.entity.LatestComment;
import com.bgg.entity.Reply;
import com.bgg.mapper.CommentMapper;
import com.bgg.service.CommentService;
import com.bgg.utils.LocalTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findCommentsByBlogId(Long id) {
        return commentMapper.findCommentsByBlogId(id);
    }

    @Override
    public List<Reply> findRepliesByCommentId(Long id) {
        return commentMapper.findRepliesByCommentId(id);
    }

    @Override
    public void addComment(CommentOpr commentOpr) {
        commentOpr.setTime(LocalTimeUtils.getDateTime());
        commentMapper.addComment(commentOpr);
    }

    @Override
    public void addReply(Reply reply) {
        reply.setTime(LocalTimeUtils.getDateTime());
        commentMapper.addReply(reply);
    }

    @Override
    public List<LatestComment> findLatestComments(){
        return commentMapper.findLatestComments();
    }

    @Override
    public void deleteComment(Long id) {
        List<Long> repliesId = commentMapper.findReplyIdByCommentId(id);
        repliesId.forEach(replyId->{
            deleteReply(replyId);
        });
        commentMapper.deleteComment(id);
    }

    @Override
    public void deleteReply(Long id) {
        commentMapper.deleteReply(id);
    }

}
