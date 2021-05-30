package com.bgg.service;

import com.bgg.dto.UpdatePasswordDto;
import com.bgg.entity.User;
import com.bgg.entity.UserAvatar;
import com.bgg.entity.UserInfo;
import org.apache.ibatis.annotations.Result;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author bgg
 * @since 2021-03-14
 */
public interface UserService {
    User findUserById(long id);

    User findUserByUsername(String username);

    void addUser(User user);

    void updatePassword(UpdatePasswordDto updatePasswordDto);

    void updateAvatar(UserAvatar userAvatar);

    String findAvatar(Long id);

    void updateUserInfo(UserInfo userInfo);


}
