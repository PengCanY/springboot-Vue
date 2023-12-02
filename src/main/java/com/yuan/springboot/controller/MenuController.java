package com.yuan.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuan.springboot.common.Constants;
import com.yuan.springboot.common.Result;
import com.yuan.springboot.entity.Dict;
import com.yuan.springboot.entity.Menu;
import com.yuan.springboot.entity.Role;
import com.yuan.springboot.mapper.DictMapper;
import com.yuan.springboot.service.impl.MenuServiceImpl;
import com.yuan.springboot.service.impl.RoleServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private MenuServiceImpl menuService;
    @Resource
    private DictMapper dictMapper;

    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(null));
    }



    //查找
    @GetMapping("/find")
    public Result findAll(@RequestParam(defaultValue = "") String name){
        return Result.success(menuService.findMenus(name));
    }
    //保存
    @PostMapping("/save")
    public Result save(@RequestBody Menu menu){
        return Result.success(menuService.saveMenu(menu));
    }
    //删除数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(menuService.removeById(id));
    }
    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(menuService.removeByIds(ids));
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam (defaultValue = "") String name
    ){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(menuService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }
}
