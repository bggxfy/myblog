package com.bgg.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bgg.common.lang.Result;
import com.bgg.dto.UpdatePasswordDto;
import com.bgg.entity.User;
import com.bgg.entity.UserInfo;
import com.bgg.service.UserService;
import com.bgg.shiro.AccountProfile;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author bgg
 * @since 2021-03-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequiresAuthentication
    @PostMapping("/updatePassword")
    public Result updatePassword(@Validated @RequestBody UpdatePasswordDto updatePasswordDto){
        String md5Password = new Md5Hash(updatePasswordDto.getPassword()).toHex();
        updatePasswordDto.setPassword(md5Password);
        userService.updatePassword(updatePasswordDto);
        return Result.success("修改密码成功");
    }

    @GetMapping("/findAccountProfile")
    public Result findAccountProfile(Long id){
        User user = userService.findUserById(id);
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user,profile);
        return Result.success(profile);
    }

    @RequiresAuthentication
    @PostMapping("/updateUserInfo")
    public Result updateInfo(@Validated @RequestBody UserInfo userInfo){
        System.out.println(userInfo);
        userService.updateUserInfo(userInfo);
        User user = userService.findUserById(userInfo.getId());
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user,profile);
        return Result.success(profile);
    }
}
