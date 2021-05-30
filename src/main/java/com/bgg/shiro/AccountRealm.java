package com.bgg.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.bgg.entity.User;
import com.bgg.service.UserService;
import com.bgg.utils.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {        //告诉支持的是jwtToken，而不是其他token
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;

        //getSubject()方法 获取userId
        String userId = jwtUtils.getClaimByToken(String.valueOf(jwtToken.getPrincipal())).getSubject();

        //根据id查找用户是否存在
        User user = userService.findUserById(Long.valueOf(userId));
        if(user == null){
            throw new UnknownAccountException("账户不存在");
        }
        if(user.getStatus() == -1){
            throw new LockedAccountException("账户已被锁定");
        }

        //用户存在且正常状态

        //封装非隐私数据信息的对象
        AccountProfile profile = new AccountProfile();
        //复制user的属性到profile中
        BeanUtil.copyProperties(user,profile);

        //profile作为principal
        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),this.getName());
    }
}
