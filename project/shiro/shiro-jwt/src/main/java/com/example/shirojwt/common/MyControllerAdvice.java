package com.example.shirojwt.common;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 全局异常处理器
@ControllerAdvice
public class MyControllerAdvice {

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        LOGGER.error("发生异常", ex);
        Result result = Result.fail("服务暂不可用");
        if (ex instanceof MyException){
            result.setStatus(((MyException) ex).getCode());
            result.setMessage(ex.getMessage());
        }else if (ex instanceof UnauthorizedException){
            result.setMessage("无此权限");
        }else if (ex instanceof NullPointerException){
            result.setMessage("空指针异常");
        }
        return result;
    }

}
