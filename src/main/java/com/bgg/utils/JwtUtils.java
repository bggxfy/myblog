package com.bgg.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "bgg.jwt")
public class JwtUtils {

    private String secret;      //密钥
    private long expire;
    private String header;

    /**
     * 生成jwt token  （头部，载荷，签证）
     */
    public String generateToken(long userId) {

        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);      //nowDate.getTime()得到当前的毫秒时间

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId + "")    //将用户id设置为subject
                .setIssuedAt(nowDate)       //设置签发时间
                .setExpiration(expireDate)  //设置超时时间
                .signWith(SignatureAlgorithm.HS512, secret)     //设置加密算法和密钥
                .compact();

        log.info("生成的token", token);
        return token;
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)      //密钥
                    .parseClaimsJws(token)      //token
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
