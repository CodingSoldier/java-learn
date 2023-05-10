package com.example.thingdemo.exception;


public class AppException extends RuntimeException {

    private final Integer code;
    private final String message;

    public AppException(String message) {
        super(message);
        this.code = 400;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}