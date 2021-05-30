package com.bgg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Reply {

    private Long id;    //回复id
    private Long userId;   //用户id
    private String name;    //用户姓名
    private String headImg;     //头像地址
    private String content;    //回复内容
    private LocalDateTime time;  //回复时间
    private Long toId;      //回复评论的id
    private Long stars;     //点赞数

    private Long toReplyId; //回复 回复的id
    private String toReplyName; //回复 回复的人姓名

}
