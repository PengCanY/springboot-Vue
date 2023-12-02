package com.yuan.springboot.controller.dto;

import com.yuan.springboot.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
* 接受前端登录请求的参数
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String token;
    private String role;
    private List<Menu> menus;
}
