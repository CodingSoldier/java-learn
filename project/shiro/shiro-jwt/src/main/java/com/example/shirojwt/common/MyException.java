package com.example.shirojwt.common;

import lombok.Data;

/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@Data
public class MyException extends RuntimeException {

    private Integer code = 0;

    public MyException(String message) {
        super(message);
    }

    public MyException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
