package com.example.thingdemo.constant;

/**
 * @author chenpq
 * @since 2022/2/8 14:19
 */
public class TopicConstant {

    /**
     * 设置设备属性，请求topic
     */
    public static final String PROPERTY_SET = "sys/${productKey}/${deviceCode}/thing/property/set";

    /**
     * 设置设备属性，响应topic
     */
    public static final String PROPERTY_SET_REPLY = "sys/${productKey}/${deviceCode}/thing/property/set_reply";

    /**
     * 设置设备上报，请求topic
     */
    public static final String PROPERTY_POST = "sys/${productKey}/${deviceCode}/thing/property/post";

    /**
     * 设置设备上报，响应topic
     */
    public static final String PROPERTY_POST_REPLY = "sys/${productKey}/${deviceCode}/thing/property/post_reply";

    /**
     * 事件上报，请求topic
     */
    public static final String EVENT_POST = "sys/${productKey}/${deviceCode}/thing/event/${tsl.service.identifier}/post";

    /**
     * 事件上报，响应topic
     */
    public static final String EVENT_POST_REPLY = "sys/${productKey}/${deviceCode}/thing/event/${tsl.service.identifier}/post_reply";

    /**
     * 服务调用，请求topic
     */
    public static final String SERVICE = "sys/${productKey}/${deviceCode}/thing/service/${tsl.service.identifier}";

    /**
     * 服务调用，响应topic
     */
    public static final String SERVICE_REPLY = "sys/${productKey}/${deviceCode}/thing/service/${tsl.service.identifier}_reply";

}
