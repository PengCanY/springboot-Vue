package com.yuan.springboot.controller;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuan.springboot.common.Constants;
import com.yuan.springboot.common.Result;
import com.yuan.springboot.controller.dto.UserDTO;
import com.yuan.springboot.entity.User;
import com.yuan.springboot.mapper.UserMapper;

import com.yuan.springboot.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
@RestController
@Log4j
//统一给接口加前缀注意规范前面少一个斜杠可以，后面不可以多斜杠
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;
    @Autowired
    private UserMapper userMapper;
    //新增和修改
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
       String username = userDTO.getUsername();
       String password = userDTO.getPassword();
       if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
           return Result.error(Constants.COOE_400,"参数错误");
       }
         UserDTO dto= userService.login(userDTO);
       return Result.success(dto);
    }
    //保存
    @PostMapping("/save")
    public Result save(@RequestBody User user){
     return Result.success( userService.saveUser(user));
    }
    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.COOE_400,"参数错误");
        }
     return Result.success(userService.register(userDTO));
    }
    //查询所有
    @GetMapping("/all")
    public Result findAll(){
        return Result.success(userService.list());
    }
    //
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }
    //删除数据
    @DeleteMapping("/{id}")
   public Result delete(@PathVariable Integer id){
        return Result.success(userService.removeById(id));
    }
    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(userService.removeByIds(ids));
    }
    //mybatis-plus 的分页查询
    //(defaultValue = "")默认值为空
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam (defaultValue = "") String username,
                                @RequestParam (defaultValue = "") String nickname,
                                @RequestParam (defaultValue = "") String email,
                                @RequestParam  (defaultValue = "") String address
                               ){

        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            queryWrapper.like("username",username);
        }
         if(!"".equals(nickname)){
             queryWrapper.like("nickname",nickname);
        }
         if(!"".equals(email)){
             queryWrapper.like("email",email);
        }
         if(!"".equals(address)){
             queryWrapper.like("address",address);
        }
         queryWrapper.orderByDesc("id");
        return Result.success(userService.page(page,queryWrapper));
    }
//分页查询
//@RequestParam接受 ?pageNum =1&pageSize=10
//作用将传过来的pageNum =1&pageSize=10映射到函数中
//limit第一个参数 = （pageNum - 1）* pageSize
//pageSize = 每页个数
//         @GetMapping("/page")
//       public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                           @RequestParam Integer pageSize,
//                                           @RequestParam String username,
//                                           @RequestParam String email,
//                                           @RequestParam String address){
//             pageNum = (pageNum - 1)*pageSize;
//             List<User> data = userMapper.selectPage(pageNum, pageSize,username,email,address);
//             Integer total = userMapper.selectTotal(username,email,address);
//             Map<String, Object> res = new HashMap<>();
//             res.put("data",data);
//             res.put("total",total);
//             return res;
//    }
   /**
   * 导出接口
   * */
    @GetMapping("/export")
   public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<User> list =userService.list();
        //通过工具类创建writer写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
       //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","ID");
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("avatar","头像");
        writer.addHeaderAlias("createTime","创建时间");
        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition","attachment;filename"+fileName+".xlsx");


        ServletOutputStream out = response.getOutputStream();
         writer.flush(out,true);
         out.close();
         writer.close();



}

/*
* excle 导入
* @param file
* @throws Exception
 */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception{
        InputStream inputStream =file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
       //方式一
       // List<User> list = reader.readAll(User.class);
        //方式二要去掉id那一列
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list){
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatar(row.get(6).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return Result.success(true);
    }
}


