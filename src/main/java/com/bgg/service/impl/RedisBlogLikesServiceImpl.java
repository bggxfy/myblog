package com.bgg.service.impl;

import com.bgg.entity.BlogLike;
import com.bgg.entity.BlogLikeCount;
import com.bgg.service.BlogLikesService;
import com.bgg.service.RedisBlogLikesService;
import com.bgg.utils.RedisBlogLikesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisBlogLikesServiceImpl implements RedisBlogLikesService {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    BlogLikesService blogLikesService;

    @Override
    public void userLikeBlog(BlogLike blogLike) {
        String blogId = blogLike.getBlogId().toString();
        String userId = blogLike.getUserId().toString();
        String blogLikedKey = RedisBlogLikesUtils.getBlogLikedKey(blogId, userId);
        if(("0").equals(stringRedisTemplate.opsForHash().get(RedisBlogLikesUtils.BLOG_LIKES, blogLikedKey))
                ||stringRedisTemplate.opsForHash().get(RedisBlogLikesUtils.BLOG_LIKES,blogLikedKey)==null){
            stringRedisTemplate.opsForHash().put(RedisBlogLikesUtils.BLOG_LIKES, blogLikedKey, "1");
            incrementLikedCount(blogLike.getBlogId());
        }else{
            throw new RuntimeException("点赞异常,请重试");
        }
    }

    @Override
    public void userUnLikeBlog(BlogLike blogLike) {
        String blogId = blogLike.getBlogId().toString();
        String userId = blogLike.getUserId().toString();
        String blogLikedKey = RedisBlogLikesUtils.getBlogLikedKey(blogId, userId);
        if(("1").equals(stringRedisTemplate.opsForHash().get(RedisBlogLikesUtils.BLOG_LIKES, blogLikedKey))){
            stringRedisTemplate.opsForHash().put(RedisBlogLikesUtils.BLOG_LIKES, blogLikedKey, "0");
            decrementLikedCount(blogLike.getBlogId());
        }
        else{
            throw new RuntimeException("点赞异常,请重试");
        }
    }

    @Override
    public void deleteLikedFromRedis(BlogLike blogLike) {
        String blogId = blogLike.getBlogId().toString();
        String userId = blogLike.getUserId().toString();
        String blogLikedKey = RedisBlogLikesUtils.getBlogLikedKey(blogId, userId);
        stringRedisTemplate.opsForHash().delete(RedisBlogLikesUtils.BLOG_LIKES, blogLikedKey);
    }

    @Override
    public void incrementLikedCount(Long blogId) {
        stringRedisTemplate.opsForHash().increment(RedisBlogLikesUtils.BLOG_LIKES_COUNT, blogId.toString(), 1);
    }

    @Override
    public void decrementLikedCount(Long blogId) {
        stringRedisTemplate.opsForHash().increment(RedisBlogLikesUtils.BLOG_LIKES_COUNT, blogId.toString(), -1);
    }

    @Override
    public boolean isUserLikedBlog(BlogLike blogLike) {
        String blogLikedKey = RedisBlogLikesUtils.getBlogLikedKey(blogLike.getBlogId().toString(), blogLike.getUserId().toString());
        System.out.println(blogLikedKey);
        System.out.println();
        if(("1").equals(stringRedisTemplate.opsForHash().get(RedisBlogLikesUtils.BLOG_LIKES,blogLikedKey)))
        {
            return true;
        }else {
            return false;

        }
    }

    @Override
    public Long getLikedCountByBlogIdFromRedis(Long blogId) {
        String count = (String) stringRedisTemplate.opsForHash().get(RedisBlogLikesUtils.BLOG_LIKES_COUNT, blogId.toString());
        return Long.valueOf(count);
    }

    @Override
    public List<BlogLike> refreshDBLikedDataFromRedis() {
        if (stringRedisTemplate.opsForHash().keys(RedisBlogLikesUtils.BLOG_LIKES).isEmpty())
            return null;
        List<BlogLike> list = new ArrayList<>();
        //迭代hashkey
        try {
            Cursor cursor = stringRedisTemplate.opsForHash().scan(RedisBlogLikesUtils.BLOG_LIKES, ScanOptions.NONE);
            while (cursor.hasNext()) {
                Map.Entry entry = (Map.Entry) cursor.next();
                String key = (String) entry.getKey();   //获取blogId:userId
                String[] split = key.split(":");
                Long blogId = Long.valueOf(split[0]);
                Long userId = Long.valueOf(split[1]);
                Integer isLiked = (Integer) entry.getValue();
                BlogLike blogLike = new BlogLike();
                blogLike.setBlogId(blogId).setBlogId(userId).setIsLiked(isLiked);
                list.add(blogLike);     //存入list后删除redis中的数据
                stringRedisTemplate.opsForHash().delete(RedisBlogLikesUtils.BLOG_LIKES, key);
            }
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (stringRedisTemplate.opsForHash().keys(RedisBlogLikesUtils.BLOG_LIKES).isEmpty()) {
            stringRedisTemplate.opsForHash().delete(RedisBlogLikesUtils.BLOG_LIKES);
        }
        return list;
    }

    @Override
    public void setRedisDataFromDB() {
        if (stringRedisTemplate.opsForHash().keys(RedisBlogLikesUtils.BLOG_LIKES).isEmpty() && stringRedisTemplate.opsForHash().keys(RedisBlogLikesUtils.BLOG_LIKES_COUNT).isEmpty()) {
            //存入点赞数据
            List<BlogLike> blogLikedDataFromDB = blogLikesService.getBlogLikedDataFromDB();
            blogLikedDataFromDB.forEach(blogLike -> {
                String blogId = blogLike.getBlogId().toString();
                String userId = blogLike.getUserId().toString();
                String blogLikedKey = RedisBlogLikesUtils.getBlogLikedKey(blogId, userId);
                String isLiked = blogLike.getIsLiked().toString();
                stringRedisTemplate.opsForHash().put(RedisBlogLikesUtils.BLOG_LIKES, blogLikedKey, isLiked);
            });

            //存入点赞数量
            List<BlogLikeCount> blogLikedCountFromDB = blogLikesService.getBlogLikedCountFromDB();
            blogLikedCountFromDB.forEach(blogLikeCount -> {
                String blogId = blogLikeCount.getBlogId().toString();
                String likedNums = blogLikeCount.getLikedNums().toString();
                stringRedisTemplate.opsForHash().put(RedisBlogLikesUtils.BLOG_LIKES_COUNT, blogId, likedNums);
            });
        }

    }

    @Override
    public List<BlogLike> getLikedDataFromRedis() {
        Cursor cursor = stringRedisTemplate.opsForHash().scan(RedisBlogLikesUtils.BLOG_LIKES, ScanOptions.NONE);
        List<BlogLike> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry entry = (Map.Entry) cursor.next();
            String key = (String) entry.getKey();   //获取blogId:userId
            String[] split = key.split(":");
            Long blogId = Long.valueOf(split[0]);
            Long userId = Long.valueOf(split[1]);
            Integer isLiked = Integer.parseInt((String) entry.getValue());
            BlogLike blogLike = new BlogLike();
            blogLike.setBlogId(blogId).setUserId(userId).setIsLiked(isLiked);
            list.add(blogLike);
        }
        return list;
    }

    @Override
    public List<BlogLike> getLikedDataByBlogIdFromRedis(Long blogId) {
        List<BlogLike> list = new ArrayList<>();
        try {
            Cursor cursor = stringRedisTemplate.opsForHash().scan("BLOG_LIKES", ScanOptions.scanOptions().count(100).match(blogId + "*").build());
            while (cursor.hasNext()) {
                Map.Entry entry = (Map.Entry) cursor.next();
                String key = (String) entry.getKey();   //获取blogId:userId
                String[] split = key.split(":");
                BlogLike blogLike = new BlogLike();
                blogLike.setBlogId(Long.valueOf(split[0])).setUserId(Long.valueOf(split[1])).setIsLiked(Integer.parseInt((String) entry.getValue()));
                list.add(blogLike);
            }
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BlogLikeCount> getLikedCountFromRedis() {
        Cursor cursor = stringRedisTemplate.opsForHash().scan(RedisBlogLikesUtils.BLOG_LIKES_COUNT, ScanOptions.NONE);
        List<BlogLikeCount> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry entry = (Map.Entry) cursor.next();
            Long blogId = Long.valueOf((String) entry.getKey());   //获取blogId
            Long likedNums = Long.parseLong((String) entry.getValue());
            BlogLikeCount blogLikeCount = new BlogLikeCount();
            blogLikeCount.setBlogId(blogId).setLikedNums(likedNums);
            list.add(blogLikeCount);
        }
        return list;
    }

}
