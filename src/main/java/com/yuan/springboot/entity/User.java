package com.yuan.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
//因为mybatis-plus默认用User去数据库查找需要加上注解来识别表
@TableName("sys_user")
public class User {
    //主键需要加上id
    @TableId(type = IdType.AUTO)
    private  Integer id;
    private String username;
    //不想展示密码给忽略掉
    @JsonIgnore
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    //当属性名称与数据库中的名称字段不一致时我们可与使用
    //mybatis-plus自动转驼峰
    @TableField(value = "avatar_url")//指定数据库的字段名称
    private String avatar;
    @TableField(value = "create_name")//指定数据库的字段名称
    private Date createTime;

    private String role;
}
