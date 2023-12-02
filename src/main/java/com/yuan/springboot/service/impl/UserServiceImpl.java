package com.yuan.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuan.springboot.common.Constants;
import com.yuan.springboot.controller.dto.UserDTO;
import com.yuan.springboot.entity.Menu;
import com.yuan.springboot.entity.User;
import com.yuan.springboot.exception.ServiceException;
import com.yuan.springboot.mapper.RoleMapper;
import com.yuan.springboot.mapper.RoleMenuMapper;
import com.yuan.springboot.mapper.UserMapper;
import com.yuan.springboot.service.MenuService;
import com.yuan.springboot.service.UserService;
import com.yuan.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final Log LOG = Log.get();
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuService menuService;
    @Override
    public UserDTO login(UserDTO userDTO) {
         User one = getUserInfo(userDTO);
        if(one!=null){
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
            String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            //查询角色
            String role = one.getRole();

            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else {
            throw new ServiceException(Constants.COOE_600,"用户名或者密码错误");
        }
//        userMapper.login(userDTO.getUsername(),userDTO.getPassword());

    }
    public boolean saveUser(User user) {
//        if(user.getId() == null){
//            return save(user);//这是利用mybatis-plus的方法进行调用
//        }else {
//           return updateById(user);
//        }
        return saveOrUpdate(user);
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null){
            one = new User();
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);//把copy完之后的用户对象存储到数据库
        }else {
            throw new ServiceException(Constants.COOE_600,"用户名已存在");
        }
        return one;
    }
    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);//从数据库查询数据信息
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.COOE_500,"系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        //当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenus("");
        //new 一个筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色菜单
        for (Menu menu : menus) {
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            //removeIf() 移除children里面不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
