package com.example.iot.common;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP 接口返回的错误响应。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 便于程序识别的错误码。
     */
    private String code;

    /**
     * 便于用户理解的错误信息。
     */
    private String message;
}
