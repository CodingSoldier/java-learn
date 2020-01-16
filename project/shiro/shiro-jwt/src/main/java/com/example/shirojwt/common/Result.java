package com.example.shirojwt.common;

import lombok.Data;

/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@Data
public class Result<T> {

    private int status;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result(Constant.CODE_SUCCESS, "成功", data);
    }

    public static Result fail(){
        return new Result(Constant.CODE_FAIL, "失败", null);
    }

    public static Result fail(String message){
        return new Result(Constant.CODE_FAIL, message, null);
    }

    public static Result fail(int status, String message){
        return new Result(status, message, null);
    }

}
