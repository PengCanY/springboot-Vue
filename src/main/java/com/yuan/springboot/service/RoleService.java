package com.yuan.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuan.springboot.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role>{
    List<Integer> getRoleMenu(Integer roleId);
}
