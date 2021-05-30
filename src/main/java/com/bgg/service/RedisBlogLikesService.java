package com.bgg.service;

import com.bgg.entity.BlogLike;
import com.bgg.entity.BlogLikeCount;
import java.util.List;

public interface RedisBlogLikesService {

    void userLikeBlog(BlogLike blogLike);     //用户给博客点赞

    void userUnLikeBlog(BlogLike blogLike);   //用户取消给博客点赞

    void deleteLikedFromRedis(BlogLike blogLike);     //删除博客的某个用户点赞记录

    void incrementLikedCount(Long blogId);        //博客点赞数量加一

    void decrementLikedCount(Long blogId);        //博客点赞数量减一

    boolean isUserLikedBlog(BlogLike blogLike);     //根据用户id和评论id查询是否存在点赞记录

    List<BlogLike> refreshDBLikedDataFromRedis();       //将redis更新至数据库中

    void setRedisDataFromDB();       //丛数据库获取数据存入redis

    List<BlogLike> getLikedDataFromRedis();     //从redis中获取所有博客点赞数据

    List<BlogLike> getLikedDataByBlogIdFromRedis(Long blogId);  //获取某篇博客的点赞数据

    List<BlogLikeCount> getLikedCountFromRedis();   //从redis中获取所有博客的点赞数量

    Long getLikedCountByBlogIdFromRedis(Long blogId);      //获取某篇博客的点赞数量


}
