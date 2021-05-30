package com.bgg.mapper;

import com.bgg.entity.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author bgg
 * @since 2021-03-14
 */
@Mapper
public interface BlogMapper {
    List<Blog> findBlogsByPage(Page page);

    Integer findBlogsNumber();

    Blog findBlogById(Long id);

    void addBlog(Blog blog);

    void editBlog(Blog blog);

    void delete(Long id);

    void browseAddOne(Long id);

    List<HotBlog> findHotBlogs();
}
