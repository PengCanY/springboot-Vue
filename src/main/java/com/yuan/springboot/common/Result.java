package com.yuan.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;
    private String msg;
    //通用类型
    private Object data;
    public static Result success(){
        return new Result(Constants.COOE_200,"",null);
    }
    public static Result success(Object data){
        return new Result(Constants.COOE_200,"",data);
    }
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
    public static Result error(){
        return new Result(Constants.COOE_500,"系统错误",null);
    }
}
