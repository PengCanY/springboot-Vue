package com.yuan.springboot.exception;

import com.yuan.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
  public Result handle(ServiceException se){
      return Result.error(se.getCode(),se.getMessage());

  }
}
