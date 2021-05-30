package com.bgg.service;

import com.bgg.entity.*;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author bgg
 * @since 2021-03-14
 */
public interface BlogService {
    List<Blog> findBlogsByPage(Page page);

    Integer findBlogsNumber();

    Blog findBlogById(Long id);

    void addBlog(Blog blog);

    void editBlog(Blog blog);

    void delete(Long id);

    void browseAddOne(Long id);

    List<HotBlog> findHotBlogs();
}
