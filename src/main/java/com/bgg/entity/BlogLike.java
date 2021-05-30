package com.bgg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
/**
 * 用户点赞博客表
 */
public class BlogLike {

    @Id
    private Long id;

    private Long blogId;

    private Long userId;

    private Integer isLiked = 0;
}
