package com.yuan.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuan.springboot.entity.Menu;
import com.yuan.springboot.entity.Role;
import com.yuan.springboot.entity.RoleMenu;
import com.yuan.springboot.mapper.RoleMapper;
import com.yuan.springboot.mapper.RoleMenuMapper;
import com.yuan.springboot.service.MenuService;
import com.yuan.springboot.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Collection;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService{

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuService menuService;
    public boolean saveRole(Role role) {
//        if(user.getId() == null){
//            return save(user);//这是利用mybatis-plus的方法进行调用
//        }else {
//           return updateById(user);
//        }
        return saveOrUpdate(role);
    }

    //@Transactional 要么一起执行成功要么都不执行
    @Transactional
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        //第一种方法直接用mybatis-plus
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id",roleId);
//        roleMenuMapper.delete(queryWrapper);
        //第二种方法用sql直接写
        //都是先删除当前角色id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleId);
        //再把前端传过来的菜单id数组绑定到当前的这个角色的id上去
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if(menu.getPid() != null && !menuIds.contains(menu.getPid())){
                //二级菜单并且传过来的menuId数组里面没有他的父级id，那么我们就要补上
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
