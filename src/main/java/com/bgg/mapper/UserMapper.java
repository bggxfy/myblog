package com.bgg.mapper;

import com.bgg.dto.UpdatePasswordDto;
import com.bgg.entity.User;
import com.bgg.entity.UserAvatar;
import com.bgg.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author bgg
 * @since 2021-03-14
 */
@Mapper
public interface UserMapper {
    User findUserById(Long id);

    User findUserByUsername(String username);

    void addUser(User user);

    void updatePassword(UpdatePasswordDto updatePasswordDto);

    String findAvatar(Long id);

    void updateAvatar(UserAvatar userAvatar);

    void updateUserInfo(UserInfo userInfo);
}
