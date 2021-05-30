package com.bgg.mapper;

import com.bgg.entity.BlogLike;
import com.bgg.entity.BlogLikeCount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogLikesMapper {

    void save(BlogLike blogLike);   //保存点赞记录

    void update(BlogLike blogLike);     //修改点赞记录

    void saveCount(BlogLikeCount blogLikeCount);     //保存点赞数量

    void updateCount(BlogLikeCount blogLikeCount);      //修改点赞数量

    List<Long> getLikedUserId(String blogId);     //根据博客id查询哪些用户点过赞   返回用户的id列表

    BlogLike getByLikedDataBlogIdAndLikedUserId(BlogLike blogLike); //通过被点赞博客和点赞人id查询是否存在点赞记录

    BlogLikeCount getByLikedCountBlogId(String blogId);   //通过博客id查询在博客点赞数量表中是否存在点赞记录

    void transLikedFromRedisToDB();         //将Redis里的点赞数据存入数据库中

    void transLikedCountFromRedisToDB();    //将Redis中的点赞数量数据存入数据库

    List<BlogLike> getBlogLikedDataFromDB();     //从数据库获取所有的博客点赞数据

    List<BlogLikeCount> getBlogLikedCountFromDB();   //从数据库获取所有博客的点赞数量

}
