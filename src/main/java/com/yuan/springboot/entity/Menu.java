package com.yuan.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
public class Menu {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String  name;
    private String  path;
    private String  icon;
    private String description;
//    @TableField(exist = false)意味着实体类中存在但是数据库表中存在
//    JsonIngnore是表中有字段，只是不在前端展示
    @TableField(exist = false)
    private List<Menu> children;
    private Integer pid;
    //mybatis-plus默认开启驼峰命名
    private String pagePath;

}
