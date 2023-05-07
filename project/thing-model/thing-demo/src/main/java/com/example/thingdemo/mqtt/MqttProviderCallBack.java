package com.example.thingdemo.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author chenpq05
 * @since 2023/4/11 14:14
 * <p>
 * mqtt生产者回调
 */
@Slf4j
public class MqttProviderCallBack implements MqttCallback {


    /**
     * 与服务器断开连接的回调
     *
     * @param throwable
     * @return void
     * @author xct
     * @date 2021/7/30 16:19
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("与服务器断开连接", throwable);
    }

    /**
     * 消息到达的回调
     *
     * @param s
     * @param mqttMessage
     * @return void
     * @author xct
     * @date 2021/7/30 16:19
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("消息到达的回调", s);
    }

    /**
     * 消息发布成功的回调
     *
     * @param iMqttDeliveryToken
     * @return void
     * @author xct
     * @date 2021/7/30 16:20
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        IMqttAsyncClient client = iMqttDeliveryToken.getClient();
        log.info(client.getClientId() + "发布消息成功！");
    }

}
