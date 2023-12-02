package com.yuan.springboot.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
* 自定义异常
* */

@Getter
public class ServiceException extends RuntimeException {
  private String code;
  public ServiceException(String code,String msg){
      super(msg);
      this.code = code;
  }
}
