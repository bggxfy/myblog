package com.bgg.utils;

public class RedisBlogLikesUtils {

    //保存博客点赞数据的key
    public static final String BLOG_LIKES = "BLOG_LIKES";
    //保存博客点赞数量的key
    public static final String BLOG_LIKES_COUNT = "BLOG_LIKES_COUNT";

    //拼接成   ”博客id：用户id“的hashKey
    public static String getBlogLikedKey(String blogId,String userId){
        StringBuilder builder = new StringBuilder();
        builder.append(blogId).append(":").append(userId);
        return builder.toString();
    }

}
