package com.demo.common;

import lombok.Data;

/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@Data
public class Result<T> {

    private Integer status;
    private String message;
    private T data;

    public Result() {
    }

    public Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result(1, "成功", data);
    }

    public static Result fail(){
        return new Result(0, "失败", null);
    }

    public static Result fail(String message){
        return new Result(0, message, null);
    }

    public static Result fail(Integer status, String message){
        return new Result(status, message, null);
    }

}
