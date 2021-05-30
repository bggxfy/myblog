package com.bgg.shiro;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String avatar;

    private String email;

    private Integer role;

    private LocalDateTime created;

}
