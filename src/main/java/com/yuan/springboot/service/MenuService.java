package com.yuan.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuan.springboot.entity.Menu;
import com.yuan.springboot.entity.Role;

import java.util.List;

public interface MenuService extends IService<Menu> {
    boolean saveMenu(Menu menu);

    List<Menu> findMenus(String name);
}
