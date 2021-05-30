package com.bgg.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordDto {

    private Long id;

    @NotBlank(message = "密码不能为空")
    private String password;

}
