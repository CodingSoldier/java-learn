package com.example.thingdemo.mqtt;

import com.example.thingdemo.cache.TingCache;
import com.example.thingdemo.constant.TopicConstant;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.protocol.TingReq;
import com.example.thingdemo.service.TingService;
import com.example.thingdemo.util.CommonUtil;
import com.example.thingdemo.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author cpq
 * @since 2023-04-11
 * mq连接工厂
 */
@Slf4j
@Component
public class MqttProviderSender {

    @Autowired
    private MqttProviderConfig mqttProviderConfig;
    @Autowired
    private TingService tingService;

    private void publish(int qos, String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(false);
        mqttMessage.setPayload(message.getBytes());
        //主题目的地，用于发布/订阅消息
        MqttTopic mqttTopic = mqttProviderConfig.getClient().getTopic(topic);
        //提供一种机制来跟踪消息的传递进度。
        //用于在以非阻塞方式（在后台运行）执行发布时跟踪消息的传递进度
        MqttDeliveryToken token;
        //将指定消息发布到主题，但不等待消息传递完成。返回的token可用于跟踪消息的传递状态。
        //一旦此方法干净地返回，消息就已被客户端接受发布。当连接可用时，将在后台完成消息传递。
        try {
            log.info("发送mqtt消息，topic={}, message={}", topic, message);
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion(1000 * 60);
        }catch (MqttException e) {
            log.error("", e);
            throw new AppException("发送数据到mqtt异常");
        }

    }

    /**
     * 设置属性
     * @param productKey
     * @param deviceCode
     * @param params
     * @return 消息id
     */
    public String propertySet(String productKey, String deviceCode, Map<String, Object> params) {
        TingCache tingCache = tingService.getTingCache(productKey);
        if (tingCache == null) {
            throw new AppException("设置属性失败，找不到物模型信息");
        }
        String topic = TopicConstant.PROPERTY_SET
            .replace("${productKey}", productKey)
            .replace("${deviceCode}", deviceCode);
        final String id = CommonUtil.uuid32();
        TingReq tingReq = new TingReq(id, tingCache.getProfile().getVersion(),
            params);
        String msg = ObjectMapperUtil.writeValueAsString(tingReq);
        publish(1, topic, msg);
        return id;
    }

}
