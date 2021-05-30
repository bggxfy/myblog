package com.bgg.service.impl;

import com.bgg.dto.UpdatePasswordDto;
import com.bgg.entity.User;
import com.bgg.entity.UserAvatar;
import com.bgg.entity.UserInfo;
import com.bgg.mapper.UserMapper;
import com.bgg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bgg
 * @since 2021-03-14
 */
@Service
//public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(long id) {
        return userMapper.findUserById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }

    @Override
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {

        userMapper.updatePassword(updatePasswordDto);
    }

    @Override
    public void updateAvatar(UserAvatar userAvatar) {
        userMapper.updateAvatar(userAvatar);
    }

    @Override
    public String findAvatar(Long id) {
        return userMapper.findAvatar(id);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        userMapper.updateUserInfo(userInfo);
    }


}
