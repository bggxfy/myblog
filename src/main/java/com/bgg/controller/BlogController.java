package com.bgg.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bgg.common.lang.Result;
import com.bgg.entity.Blog;
import com.bgg.entity.Comment;
import com.bgg.entity.HotBlog;
import com.bgg.entity.Page;
import com.bgg.service.BlogService;
import com.bgg.utils.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bgg
 * @since 2021-03-14
 */
@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
        Page page = new Page(currentPage,5);
        List<Blog> pageData = blogService.findBlogsByPage(page);
        return Result.success(pageData);
    }

    @GetMapping("/blogsNumber")
    public Result number(){
        Integer number = blogService.findBlogsNumber();
        return Result.success(number);
    }

    @GetMapping("/blog/{id}")
    public Result list(@PathVariable(name = "id") long id){
        Blog blog = blogService.findBlogById(id);
        Assert.notNull(blog,"该博客已被删除");

        return Result.success(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result list(@Validated @RequestBody Blog blog){

        Blog temp = null;

        //blog id不为空是修改   为空则为新增
        if(blog.getId() != null){
            temp = blogService.findBlogById(blog.getId());
            //只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(),"没有权限编辑");
            //复制被修改的属性  忽略不需要修改的属性
            BeanUtil.copyProperties(blog,temp,"id","userId","created","status");
            blogService.editBlog(temp);

        }else {
            //新增
            blog.setUserId(ShiroUtil.getProfile().getId()).setCreated(LocalDateTime.now()).setStatus(0);
            //复制被修改的属性  忽略不需要修改的属性
            blogService.addBlog(blog);
        }

        return Result.success(null);
    }

    @PostMapping("/blog/delete")
    @RequiresAuthentication
    public Result delete(@RequestBody Map<String,Long> params){
        Long id = params.get("id");
        if (id == null) {
            throw new RuntimeException("删除的博客id不能为空");
        }

        blogService.delete(id);
        return Result.success("删除成功！");
    }

    @PostMapping("/blog/addBrowse")
    public Result browseAddOne(@RequestBody Map<String,Long> params){
        Long id = params.get("id");
        if (id == null) {
            throw new RuntimeException("博客id异常");
        }

        blogService.browseAddOne(id);
        return Result.success("新增浏览成功！");
    }

    @GetMapping("/blog/hotBlogs")
    public Result hotBlogs(){
        List<HotBlog> hotBlogs = blogService.findHotBlogs();
        return Result.success(hotBlogs);
    }


}
