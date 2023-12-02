package com.yuan.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuan.springboot.controller.dto.UserDTO;
import com.yuan.springboot.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
//通过@Mapper来注入mapper接口
//也可以通过@MapperScan加（路径（com那种））来注入
//@Mapper

//@Repository//代表持久层
public interface UserMapper extends BaseMapper<User> {
   // 继承了BaseMapper<User>所以不需要重写方法，里面提供了
    @Select("SELECT * from sys_user where true")
    List<User> findAll();
    int insert(User user);
    int update(User user);
    //@Param("id")让框架知道参数是什么名字
    @Delete("delete from sys_user where id = #{id}")
    Integer deleteById(@Param("id") Integer id);
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND password = #{password}")
    UserDTO login(@Param("username") String username, @Param("password") String password);
//   @Select("select * from sys_user where username like concat('%',#{username},'%') and email like concat('%',#{email},'%') and address like concat('%',#{address},'%') limit #{pageNum},#{pageSize}")
//    List<User> selectPage(Integer pageNum, Integer pageSize,String username,String email,String address);
//    @Select("select count(*) from sys_user where username like concat('%',#{username},'%') and email like concat('%',#{email},'%') and address like concat('%',#{address},'%')")
//    Integer selectTotal(String username,String email,String address);
}
