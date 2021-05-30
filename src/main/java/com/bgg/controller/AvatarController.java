package com.bgg.controller;

import com.bgg.common.lang.Result;
import com.bgg.entity.UserAvatar;
import com.bgg.service.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    @Autowired
    private UserService userService;

    /**
     * 头像上传
     *
     * @param id
     * @param avatar
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(Long id, @RequestParam("avatar") MultipartFile avatar) {

        //找到旧的头像名
        String oldAvatar = userService.findAvatar(id);
        //拼接头像名 id+头像原始名
        String avatarName = id + avatar.getOriginalFilename();

        System.out.println(avatarName);

        File path = null;
        try {
            //获取与jar包同级的目录
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!path.exists()) {
            path = new File("");
        }


        File avatarFile = new File(path.getAbsolutePath(),"static/avatar/"+avatarName);

        //在指定路径下创建一个文件
        if (!avatarFile.exists() && !avatarFile.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + avatarFile);
            avatarFile.mkdirs();
        }

        //将文件保存到服务器指定位置
        try {
            avatar.transferTo(avatarFile);
            System.out.println("上传头像成功");

            UserAvatar userAvatar = new UserAvatar();
            userAvatar.setId(id).setAvatar(avatarName);
            //头像路径存入数据库
            userService.updateAvatar(userAvatar);

            //删除旧的头像
            //非默认头像
            if (!oldAvatar.equals("default.png")) {
                //删除旧的头像
                File file = new File(path.getAbsolutePath()+"static/avatar/", oldAvatar);
                if (file.exists()) {
                    //立即删除
                    file.delete();
                }
            }
            return Result.success("上传头像成功");
        } catch (IOException e) {
            System.out.println("上传头像失败");
            e.printStackTrace();
            return Result.fail("上传头像失败");
        }
    }

    /**
     * 头像下载
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable(name = "id") Long id, HttpServletResponse response) throws IOException {


        //获取头像名
        String avatar = userService.findAvatar(id);

        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) {
            path = new File("");
        }
        File avatarFile = new File(path.getAbsolutePath(),"static/avatar/"+avatar);

        InputStream is = new FileInputStream(avatarFile);

        //附件显示
        response.setHeader("content-disposition", "inline" + ";filename=" + URLEncoder.encode(avatar, "UTF-8"));

        //获取响应输出流
        ServletOutputStream os = response.getOutputStream();
        //文件拷贝
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }
}
