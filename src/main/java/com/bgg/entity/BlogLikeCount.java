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
 * 博客点赞数量表
 */
public class BlogLikeCount {

    @Id
    private Long blogId;

    private Long likedNums;
}
