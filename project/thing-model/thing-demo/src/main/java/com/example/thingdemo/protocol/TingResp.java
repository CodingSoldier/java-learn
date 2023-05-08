package com.example.thingdemo.protocol;

import java.io.Serializable;

/**
 * @author chenpq05
 * @since 2022/2/23 14:14
 */
public class TingResp<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 消息id号，用户自定义，String类型的数字，长度限制不超过32位。
    private String id;

    // 200表示成功
    private String code;

    private String message;

    // 物模型版本
    private String version;

    @SuppressWarnings("squid:S1948")
    private T data;

    public TingResp() {
    }

    public TingResp(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TingResp{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", version='" + version + '\'' +
                ", data=" + data +
                '}';
    }
}
