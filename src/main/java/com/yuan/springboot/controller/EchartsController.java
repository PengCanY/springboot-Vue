package com.yuan.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.io.unit.DataUnit;
import com.yuan.springboot.common.Result;
import com.yuan.springboot.entity.User;
import com.yuan.springboot.service.UserService;
import com.yuan.springboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/example")
    public Result get(){
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "aaSun"));
        map.put("y", CollUtil.newArrayList("150", "230", "224", "218", "135", "147", "260"));
        return Result.success(map);
    }
    @GetMapping("members")
    public Result members(){
        List<User> list = userService.list();
        int q1 = 1;//第一季度
        int q2 = 1;//第二季度
        int q3 = 1;//第三季度
        int q4 = 1;//第四季度
        for (User user:list){
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter){
                case Q1 :q1 += 1;break;
                case Q2 :q2 += 1;break;
                case Q3 :q3 += 1;break;
                case Q4 :q4 += 1;break;
                default:break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
}
