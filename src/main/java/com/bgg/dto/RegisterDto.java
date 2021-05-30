package com.bgg.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterDto {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "昵称不能为空")
    private String name;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    private String email;

}
