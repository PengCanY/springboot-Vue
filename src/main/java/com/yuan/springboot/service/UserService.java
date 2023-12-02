package com.yuan.springboot.service;

import com.yuan.springboot.controller.dto.UserDTO;
import com.yuan.springboot.entity.User;

public interface UserService  {
    UserDTO login(UserDTO userDTO);
    boolean saveUser(User user);
    User register(UserDTO userDTO);


}
