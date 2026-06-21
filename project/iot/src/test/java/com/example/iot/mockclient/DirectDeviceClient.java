package com.example.iot.mockclient;

import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 模拟直连设备客户端。
 * <p>
 * 模拟 light/light001 直连设备，订阅下行 Topic，响应服务调用和属性设置，
 * 定时上报属性和事件。
 * <p>
 * 如果平台下发的 payload 包含"不回复"，则不回复，用于模拟设备响应超时。
 */
public class DirectDeviceClient {

    private static final String PRODUCT_KEY = "light";
    private static final String DEVICE_CODE = "light001";
    private static final String PREFIX = "sys/v1/products/" + PRODUCT_KEY + "/devices/" + DEVICE_CODE;

    private final Mqtt5AsyncClient client;
    private final AtomicLong msgIdGen = new AtomicLong(1000);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public DirectDeviceClient(String host, int port) {
        this.client = MqttClient.builder()
                .useMqttVersion5()
                .identifier("mock-direct-" + DEVICE_CODE)
                .serverHost(host)
                .serverPort(port)
                .buildAsync();
    }

    /**
     * 启动客户端：连接、订阅、注册消息回调、启动定时上报。
     */
    public void start() throws Exception {
        client.connectWith().send().get();
        System.out.println("[直连设备] " + PRODUCT_KEY + "/" + DEVICE_CODE + " 已连接");

        String downFilter = PREFIX + "/down/#";
        client.subscribeWith()
                .topicFilter(downFilter)
                .qos(MqttQos.AT_LEAST_ONCE)
                .send()
                .get();
        System.out.println("[直连设备] 已订阅: " + downFilter);

        client.publishes(MqttGlobalPublishFilter.ALL, this::handleMessage);

        scheduler.scheduleAtFixedRate(this::reportProperties, 0, 1, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(this::reportEvent, 2, 5, TimeUnit.MINUTES);
    }

    /**
     * 处理下行消息，按 Topic 路径分发。
     */
    private void handleMessage(Mqtt5Publish publish) {
        String topic = publish.getTopic().toString();
        String payload = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
        System.out.println("[直连设备] 收到消息: topic=" + topic + ", payload=" + payload);

        if (topic.contains("/services/") && topic.endsWith("/request")) {
            handleServiceRequest(topic, payload);
        } else if (topic.contains("/properties/set/") && topic.endsWith("/request")) {
            handlePropertySetRequest(payload);
        }
    }

    /**
     * 处理服务调用请求，提取 serviceCode 后回复。
     */
    @SuppressWarnings("unchecked")
    private void handleServiceRequest(String topic, String payload) {
        try {
            if (payload.contains("不回复")) {
                System.out.println("[直连设备] 收到「不回复」指令，跳过响应以模拟超时");
                return;
            }

            Map<String, Object> request = ObjectMapperUtil.readValue(payload, Map.class);
            String msgId = (String) request.get("msgId");
            Map<String, Object> data = (Map<String, Object>) request.getOrDefault("data", Map.of());

            // sys/v1/products/{pk}/devices/{dc}/down/services/{sc}/request → index 8 = serviceCode
            String[] parts = topic.split("/", -1);
            String serviceCode = parts[8];

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("msgId", msgId);
            response.put("timestamp", Instant.now().toEpochMilli());
            response.put("code", 20000);
            response.put("message", "成功");
            response.put("data", data);

            String responseTopic = PREFIX + "/up/services/" + serviceCode + "/response";
            publish(responseTopic, response);
            System.out.println("[直连设备] 服务响应已发送: serviceCode=" + serviceCode + ", msgId=" + msgId);
        } catch (Exception e) {
            System.err.println("[直连设备] 处理服务请求异常: " + e.getMessage());
        }
    }

    /**
     * 处理属性设置请求，逐属性回复 results。
     */
    @SuppressWarnings("unchecked")
    private void handlePropertySetRequest(String payload) {
        try {
            if (payload.contains("不回复")) {
                System.out.println("[直连设备] 收到「不回复」指令，跳过响应以模拟超时");
                return;
            }

            Map<String, Object> request = ObjectMapperUtil.readValue(payload, Map.class);
            String msgId = (String) request.get("msgId");
            Map<String, Object> properties = (Map<String, Object>) request.getOrDefault("properties", Map.of());

            List<Map<String, Object>> results = new ArrayList<>();
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("propertyCode", entry.getKey());
                result.put("code", 20000);
                result.put("message", "成功");
                results.add(result);
            }

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("msgId", msgId);
            response.put("timestamp", Instant.now().toEpochMilli());
            response.put("code", 20000);
            response.put("message", "成功");
            response.put("results", results);

            String responseTopic = PREFIX + "/up/properties/set/response";
            publish(responseTopic, response);
            System.out.println("[直连设备] 属性设置响应已发送: msgId=" + msgId);
        } catch (Exception e) {
            System.err.println("[直连设备] 处理属性设置异常: " + e.getMessage());
        }
    }

    /**
     * 定时上报属性：power、brightness。
     */
    private void reportProperties() {
        try {
            Map<String, Object> properties = new LinkedHashMap<>();
            properties.put("power", true);
            properties.put("brightness", ThreadLocalRandom.current().nextInt(0, 101));

            Map<String, Object> report = new LinkedHashMap<>();
            report.put("msgId", String.valueOf(msgIdGen.incrementAndGet()));
            report.put("timestamp", Instant.now().toEpochMilli());
            report.put("properties", properties);

            publish(PREFIX + "/up/properties/report", report);
            System.out.println("[直连设备] 属性已上报: " + properties);
        } catch (Exception e) {
            System.err.println("[直连设备] 属性上报异常: " + e.getMessage());
        }
    }

    /**
     * 定时上报事件：switch-alarm。
     */
    private void reportEvent() {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("switchCount", ThreadLocalRandom.current().nextInt(0, 100));
            data.put("level", "INFO");

            Map<String, Object> event = new LinkedHashMap<>();
            event.put("msgId", String.valueOf(msgIdGen.incrementAndGet()));
            event.put("timestamp", Instant.now().toEpochMilli());
            event.put("data", data);

            publish(PREFIX + "/up/events/switch-alarm/report", event);
            System.out.println("[直连设备] 事件已上报: switch-alarm");
        } catch (Exception e) {
            System.err.println("[直连设备] 事件上报异常: " + e.getMessage());
        }
    }

    private void publish(String topic, Map<String, Object> payload) throws Exception {
        byte[] json = ObjectMapperUtil.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8);
        client.publishWith()
                .topic(topic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(json)
                .send()
                .get();
    }

    public void shutdown() {
        scheduler.shutdown();
        client.disconnect();
        System.out.println("[直连设备] " + PRODUCT_KEY + "/" + DEVICE_CODE + " 已断开");
    }
}
