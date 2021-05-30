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
public class LatestComment {

    private Long blogId;
    private Long userId;
    private String name;
    private String content;
    private LocalDateTime created;

}
