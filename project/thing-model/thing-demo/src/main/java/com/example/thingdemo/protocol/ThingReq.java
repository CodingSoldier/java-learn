package com.example.thingdemo.protocol;

import java.io.Serializable;

/**
 * @author chenpq05
 * @since 2022/2/23 14:14
 */
public class ThingReq<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 消息id号，用户自定义，String类型的数字，长度限制不超过32位。
    private String id;

    // 物模型版本
    private String version;

    @SuppressWarnings("squid:S1948")
    private T params;

    public ThingReq() {
    }

    public ThingReq(String id, String version, T params) {
        this.id = id;
        this.version = version;
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ThingReq{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", params=" + params +
                '}';
    }
}
