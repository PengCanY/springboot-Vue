package com.yuan.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuan.springboot.entity.Menu;
import com.yuan.springboot.entity.Role;
import com.yuan.springboot.mapper.MenuMapper;
import com.yuan.springboot.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    public boolean saveMenu(Menu menu) {
//        if(user.getId() == null){
//            return save(user);//这是利用mybatis-plus的方法进行调用
//        }else {
//           return updateById(user);
//        }
        return saveOrUpdate(menu);
    }

    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }

        //倒序排列
//        queryWrapper.orderByDesc("id");
        //查询全部数据
        List<Menu> list = list(queryWrapper);
        //找到pid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for (Menu menu : parentNodes) {
            //删选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}
