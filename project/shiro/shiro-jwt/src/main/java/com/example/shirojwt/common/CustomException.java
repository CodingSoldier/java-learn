package com.example.shirojwt.common;

import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class CustomException extends RuntimeException {

    private Integer code = 0;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
