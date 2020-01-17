package com.example.shirojwt.common;

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
        /**
         * 不能把所有的异常信息都抛给用户，这会泄漏系统的信息
         * 发生异常时，默认提示 服务暂不可用
         */
        Result result = Result.fail("服务暂不可用");
        if (ex instanceof CustomException){
            /**
             * 异常为我们自己定义的CustomException，可将异常信息返回给用户
             * CustomException中的异常信息必须要有提示性、引导性，以便用户一看到异常信息就知道为什么会报错、该如何解决错误
             */
            result.setStatus(((CustomException) ex).getCode());
            result.setMessage(ex.getMessage());
        }else if (ex instanceof NullPointerException){
            result.setMessage("空指针异常");
        }
        return result;
    }

}
