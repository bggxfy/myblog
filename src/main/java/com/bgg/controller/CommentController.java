package com.bgg.controller;

import com.bgg.common.lang.Result;
import com.bgg.entity.Comment;
import com.bgg.entity.CommentOpr;
import com.bgg.entity.LatestComment;
import com.bgg.entity.Reply;
import com.bgg.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comment")
    public Result findCommentsByBlogId(Long id){
        if(id == null){
            throw new RuntimeException("博客id不能为空");
        }
        List<Comment> comments = commentService.findCommentsByBlogId(id);
        return Result.success(comments);
    }

    @PostMapping("/addComment")
    @RequiresAuthentication
    public Result addComment(@RequestBody CommentOpr commentOpr){
        try{
            commentService.addComment(commentOpr);
        }catch (Exception e){
            throw new RuntimeException("评论异常");
        }
        return Result.success("评论成功");
    }

    @PostMapping("/addReply")
    @RequiresAuthentication
    public Result addReply(@RequestBody Reply reply){

        System.out.println(reply);
//        try{
            commentService.addReply(reply);
//        }catch (Exception e){
//            throw new RuntimeException("回复异常");
//        }
        return Result.success("回复成功");
    }

    @GetMapping("/latestComments")
    public Result findLatestComment(){
        List<LatestComment> latestComments = commentService.findLatestComments();
        return Result.success(latestComments);
    }

    @PostMapping("/comment/deleteComment")
    @RequiresAuthentication
    public Result deleteComment(@RequestBody Map<String,Long> params){
        Long id = params.get("commentId");
        if (id == null) {
            throw new RuntimeException("删除的评论id不能为空");
        }

        commentService.deleteComment(id);
        return Result.success("删除成功！");
    }

    @PostMapping("/comment/deleteReply")
    @RequiresAuthentication
    public Result deleteReply(@RequestBody Map<String,Long> params){
        Long id = params.get("replyId");
        if (id == null) {
            throw new RuntimeException("删除的回复id不能为空");
        }

        commentService.deleteReply(id);
        return Result.success("删除成功！");
    }
}
