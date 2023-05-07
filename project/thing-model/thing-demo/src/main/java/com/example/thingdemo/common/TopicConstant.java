package com.example.thingdemo.common;

/**
 * @author chenpq
 * @since 2022/2/8 14:19
 */
public class TopicConstant {

    /**
     * 设置设备属性，请求topic
     */
    public static final String PROPERTY_SET = "sys/{productKey}/{deviceCode}/thing/property/set";

    /**
     * 设置设备属性，响应topic
     */
    public static final String PROPERTY_SET_REPLY = "sys/{productKey}/{deviceCode}/thing/property/set_reply";

    /**
     * 设置设备上报，请求topic
     */
    public static final String PROPERTY_POST = "sys/{productKey}/{deviceCode}/thing/property/post";

    /**
     * 设置设备上报，响应topic
     */
    public static final String PROPERTY_POST_REPLY = "sys/{productKey}/{deviceCode}/thing/property/post_reply";


}
