package com.bgg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentOpr implements Serializable{

    private Long id;
    private Long blogId;   //博客id
    private Long userId;   //评论者id
    private String content;     //评论内容
    private LocalDateTime time;     //评论时间


}
