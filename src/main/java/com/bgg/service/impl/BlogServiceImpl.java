package com.bgg.service.impl;

import com.bgg.entity.*;
import com.bgg.mapper.BlogMapper;
import com.bgg.service.BlogLikesService;
import com.bgg.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bgg
 * @since 2021-03-14
 */
@Service
//public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
public class BlogServiceImpl implements BlogService{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    BlogLikesService blogLikesService;

    @Override
    public List<Blog> findBlogsByPage(Page page) {
        return blogMapper.findBlogsByPage(page);
    }

    @Override
    public Integer findBlogsNumber() {
        return blogMapper.findBlogsNumber();
    }

    @Override
    public Blog findBlogById(Long id) {
        return blogMapper.findBlogById(id);
    }

    @Override
    public void addBlog(Blog blog) {
        blogMapper.addBlog(blog);
        //将 Redis 里的点赞信息同步到数据库里
        blogLikesService.transLikedFromRedisToDB();
        blogLikesService.transLikedCountFromRedisToDB();

        Set<String> keys = stringRedisTemplate.keys("*");
        stringRedisTemplate.delete(keys);
    }

    @Override
    public void editBlog(Blog blog) {
        blogMapper.editBlog(blog);
    }

    @Override
    public void delete(Long id) {
        blogMapper.delete(id);
        //将 Redis 里的点赞信息同步到数据库里
        blogLikesService.transLikedFromRedisToDB();
        blogLikesService.transLikedCountFromRedisToDB();

        Set<String> keys = stringRedisTemplate.keys("*");
        stringRedisTemplate.delete(keys);
    }

    @Override
    public synchronized void browseAddOne(Long id) {
        blogMapper.browseAddOne(id);
    }

    @Override
    public List<HotBlog> findHotBlogs() {
        return blogMapper.findHotBlogs();
    }

}
