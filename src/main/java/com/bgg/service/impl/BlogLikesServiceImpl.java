package com.bgg.service.impl;

import com.bgg.entity.BlogLike;
import com.bgg.entity.BlogLikeCount;
import com.bgg.mapper.BlogLikesMapper;
import com.bgg.service.BlogLikesService;
import com.bgg.service.RedisBlogLikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogLikesServiceImpl implements BlogLikesService {

    @Autowired
    private BlogLikesMapper blogLikesMapper;

    @Autowired
    private RedisBlogLikesService redisBlogLikesService;

    @Override
    public void save(BlogLike blogLike) {
        blogLikesMapper.save(blogLike);
    }

    @Override
    public void update(BlogLike blogLike) {
        blogLikesMapper.update(blogLike);
    }

    @Override
    public void updateCount(BlogLikeCount blogLikeCount) {
        blogLikesMapper.updateCount(blogLikeCount);
    }

    @Override
    public List<Long> getLikedUserId(String blogId) {
        return blogLikesMapper.getLikedUserId(blogId);
    }

    @Override
    public BlogLike getByLikedDataBlogIdAndLikedUserId(BlogLike blogLike) {
        return blogLikesMapper.getByLikedDataBlogIdAndLikedUserId(blogLike);
    }

//    @Override
//    public BlogLikeCount getByLikedCountBlogId(String blogId) {
//        return blogLikesMapper.getByLikedCountBlogId(blogId);
//    }

    @Override
    public void transLikedFromRedisToDB() {
        List<BlogLike> likedDataFromRedis = redisBlogLikesService.getLikedDataFromRedis();
        likedDataFromRedis.forEach(blogLike->{
            BlogLike isBlogLike = getByLikedDataBlogIdAndLikedUserId(blogLike);
            if(isBlogLike==null){   //没有记录
                save(blogLike);
            }else {     //有记录
                update(blogLike);
            }
        });
    }

    @Override
    public void transLikedCountFromRedisToDB() {
        List<BlogLikeCount> likedCountFromRedis = redisBlogLikesService.getLikedCountFromRedis();
        likedCountFromRedis.forEach(blogLikeCount->{
                updateCount(blogLikeCount);
        });
    }

    @Override
    public List<BlogLike> getBlogLikedDataFromDB() {
        return blogLikesMapper.getBlogLikedDataFromDB();
    }

    @Override
    public List<BlogLikeCount> getBlogLikedCountFromDB() {
        return blogLikesMapper.getBlogLikedCountFromDB();
    }
}
