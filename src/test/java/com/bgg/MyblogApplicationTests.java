package com.bgg;

import com.bgg.entity.Comment;
import com.bgg.entity.CommentOpr;
import com.bgg.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class MyblogApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    void contextLoads() {

    }

}
