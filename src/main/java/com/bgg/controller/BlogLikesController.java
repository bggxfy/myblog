package com.bgg.controller;

import com.bgg.common.lang.Result;
import com.bgg.entity.BlogLike;
import com.bgg.entity.BlogLikeCount;
import com.bgg.service.RedisBlogLikesService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlogLikesController {

    @Autowired
    RedisBlogLikesService redisBlogLikesService;

    @GetMapping("/redis/getLikedData")
    public Result getLikedDataFromRedis(){
        redisBlogLikesService.setRedisDataFromDB();
        List<BlogLike> likedData = redisBlogLikesService.getLikedDataFromRedis();
        return Result.success(likedData);
    }

    @GetMapping("/redis/getLikedDataByBlogID")
    public Result getLikedDataByBlogIdFromRedis(Long blogId){
        redisBlogLikesService.setRedisDataFromDB();
        List<BlogLike> likedDataByBLogId = redisBlogLikesService.getLikedDataByBlogIdFromRedis(blogId);
        return Result.success(likedDataByBLogId);
    }

    @GetMapping("/redis/getLikedCount")
    public Result getLikedCountFromRedis(){
        redisBlogLikesService.setRedisDataFromDB();
        List<BlogLikeCount> likedCount = redisBlogLikesService.getLikedCountFromRedis();
        return Result.success(likedCount);
    }

    @GetMapping("/redis/getLikedCountByBlogId")
    public Result getLikedCountByBlogIdFromRedis(Long blogId){
        redisBlogLikesService.setRedisDataFromDB();
        Long count = 0L;
        try{
            count = redisBlogLikesService.getLikedCountByBlogIdFromRedis(blogId);
        }catch (Exception e){
            throw new RuntimeException("数据异常");
        }
        return Result.success(count);
    }

    @RequiresAuthentication
    @PostMapping("/redis/isUserLikedBlog")
    public Result isUserLikedBlog(@RequestBody BlogLike blogLike){
        redisBlogLikesService.setRedisDataFromDB();
        boolean userLikedBlog = redisBlogLikesService.isUserLikedBlog(blogLike);
        return Result.success(userLikedBlog);
    }

    @RequiresAuthentication
    @PostMapping("/redis/userLikeBlog")
    public Result userLikeBlog(@RequestBody BlogLike blogLike){
        redisBlogLikesService.setRedisDataFromDB();
        redisBlogLikesService.userLikeBlog(blogLike);
        return Result.success("点赞成功");
    }

    @RequiresAuthentication
    @PostMapping("/redis/userUnLikeBlog")
    public Result userUnLikeBlog(@RequestBody BlogLike blogLike){
        redisBlogLikesService.setRedisDataFromDB();
        redisBlogLikesService.userUnLikeBlog(blogLike);
        return Result.success("取消点赞成功");
    }



}
