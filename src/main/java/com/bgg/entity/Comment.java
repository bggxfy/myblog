package com.bgg.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Comment {

    private Long id;    //评论id
    private Long blogId;   //博客id
    private Long userId;   //评论者id
    private String name;    //评论者姓名
    private String headImg;     //头像地址
    private String content;     //评论内容
    private LocalDateTime time;     //评论时间
    private Long stars;             //点赞数
    private List<Reply> replies;    //回复列表

}
