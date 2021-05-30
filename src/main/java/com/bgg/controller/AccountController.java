package com.bgg.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bgg.common.lang.Result;
import com.bgg.dto.LoginDto;
import com.bgg.dto.RegisterDto;
import com.bgg.entity.User;
import com.bgg.service.UserService;
import com.bgg.shiro.AccountProfile;
import com.bgg.utils.JwtUtils;
import com.bgg.utils.LocalTimeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){
        User user = userService.findUserByUsername(loginDto.getUsername());
        String md5Password = new Md5Hash(loginDto.getPassword()).toHex();

        if(user==null||!user.getPassword().equals(md5Password)){
            return Result.fail("用户名或密码错误");
        }

        String jwt = jwtUtils.generateToken(user.getId());

        response.setHeader("Authorization",jwt);        //jwt放回给前端
        response.setHeader("Access-control-Expose-Headers","Authorization");
        AccountProfile profile = new AccountProfile();
        //复制属性
        BeanUtil.copyProperties(user,profile);

        return Result.success(profile);
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDto registerDto){
        User hasUser = userService.findUserByUsername(registerDto.getUsername());
        if(hasUser!=null){
            return Result.fail("用户名已经存在，请重新输入");
        }
        String md5Password = new Md5Hash(registerDto.getPassword()).toHex();
        registerDto.setPassword(md5Password);
        User user = new User();
        BeanUtil.copyProperties(registerDto,user);
        user.setCreated(LocalTimeUtils.getDateTime());
        user.setAvatar("default.png");
        userService.addUser(user);
        return Result.success("注册成功");
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }

}
