package com.bgg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfo {
    private Long id;        //id
    @NotBlank(message = "昵称不能为空")
    private String name;    //昵称
    @NotBlank(message = "邮箱不能为空")
    private String email;   //邮箱

}
