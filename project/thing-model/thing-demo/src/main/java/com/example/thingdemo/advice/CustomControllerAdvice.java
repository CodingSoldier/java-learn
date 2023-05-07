/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.example.thingdemo.advice;

import com.example.thingdemo.common.Result;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 自定义异常处理
 *
 * @author chenpq05
 * @since 2022/4/18 14:37
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleAppException(AppException e) {
        log.error("业务异常", e);
        return Result.fail(e.getMessage());
    }


    /**
     * 使用 @Valid 注解，被此方法捕获
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("捕获MethodArgumentNotValidException异常", ex);
        StringBuilder sb = new StringBuilder();
        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String defaultMessage = fieldError.getDefaultMessage();
            boolean isMatch = CommonUtil.isEndWith(defaultMessage, CommonUtil.END_CHAR);
            // 没有结尾符号，添加句号
            defaultMessage = isMatch ? defaultMessage : String.format("%s。", defaultMessage);
            sb.append(defaultMessage);
        }
        String msg = sb.toString();
        return Result.fail(msg);
    }

    /**
     * 使用 @Validated 注解，被此方法捕获
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Object> constraintViolationException(ConstraintViolationException ex) {
        log.error("捕获ConstraintViolationException异常", ex);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<?> constraint : constraintViolations) {
                String message = constraint.getMessage();
                boolean isMatch = CommonUtil.isEndWith(message, CommonUtil.END_CHAR);
                // 没有结尾符号，添加句号
                message = isMatch ? message : String.format("%s。", message);
                sb.append(message);
            }
        }
        String msg = sb.toString();
        return Result.fail(400, msg);
    }


    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.fail(500, "空指针异常");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleException(Exception e) {
        log.error("服务异常", e);
        return Result.fail(500, "服务异常");
    }

}
