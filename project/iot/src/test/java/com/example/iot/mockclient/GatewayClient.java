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
 * 模拟网关客户端。
 * <p>
 * 模拟 gw001 网关，代理子设备 sensor/sensor001。
 * 订阅下行 Topic，响应服务调用和属性设置，
 * 定时上报子设备属性、事件和状态。
 * <p>
 * 如果平台下发的 payload 包含"不回复"，则不回复，用于模拟设备响应超时。
 */
public class GatewayClient {

    private static final String GATEWAY_ID = "gw001";
    private static final String SUB_PRODUCT_KEY = "sensor";
    private static final String SUB_DEVICE_CODE = "sensor001";
    private static final String PREFIX = "sys/v1/gateways/" + GATEWAY_ID;

    private final Mqtt5AsyncClient client;
    private final AtomicLong msgIdGen = new AtomicLong(2000);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public GatewayClient(String host, int port) {
        this.client = MqttClient.builder()
                .useMqttVersion5()
                .identifier("mock-gateway-" + GATEWAY_ID)
                .serverHost(host)
                .serverPort(port)
                .buildAsync();
    }

    /**
     * 启动客户端：连接、订阅、注册消息回调、上报子设备上线状态、启动定时上报。
     */
    public void start() throws Exception {
        client.connectWith().send().get();
        System.out.println("[网关] " + GATEWAY_ID + " 已连接");

        String downFilter = PREFIX + "/down/#";
        client.subscribeWith()
                .topicFilter(downFilter)
                .qos(MqttQos.AT_LEAST_ONCE)
                .send()
                .get();
        System.out.println("[网关] 已订阅: " + downFilter);

        client.publishes(MqttGlobalPublishFilter.ALL, this::handleMessage);

        // 上报子设备上线
        reportSubDeviceStatus("ONLINE");

        scheduler.scheduleAtFixedRate(this::reportSubDeviceProperties, 0, 1, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(this::reportSubDeviceEvent, 2, 5, TimeUnit.MINUTES);
    }

    /**
     * 处理下行消息，按 Topic 路径分发。
     */
    private void handleMessage(Mqtt5Publish publish) {
        String topic = publish.getTopic().toString();
        String payload = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
        System.out.println("[网关] 收到消息: topic=" + topic + ", payload=" + payload);

        if (topic.contains("/services/") && topic.endsWith("/request")) {
            handleServiceRequest(topic, payload);
        } else if (topic.contains("/properties/set/") && topic.endsWith("/request")) {
            handlePropertySetRequest(payload);
        } else if (topic.contains("/topology/sync/") && topic.endsWith("/request")) {
            handleTopologySyncRequest(payload);
        }
    }

    /**
     * 处理子设备服务调用请求，提取 serviceCode 后回复。
     */
    @SuppressWarnings("unchecked")
    private void handleServiceRequest(String topic, String payload) {
        try {
            if (payload.contains("不回复")) {
                System.out.println("[网关] 收到「不回复」指令，跳过响应以模拟超时");
                return;
            }

            Map<String, Object> request = ObjectMapperUtil.readValue(payload, Map.class);
            String msgId = (String) request.get("msgId");
            Map<String, Object> target = (Map<String, Object>) request.getOrDefault("target", Map.of());
            Map<String, Object> data = (Map<String, Object>) request.getOrDefault("data", Map.of());

            // sys/v1/gateways/{gw}/down/sub-devices/services/{sc}/request → index 7 = serviceCode
            String[] parts = topic.split("/", -1);
            String serviceCode = parts[7];

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("msgId", msgId);
            response.put("timestamp", Instant.now().toEpochMilli());
            response.put("target", target);
            response.put("code", 20000);
            response.put("message", "成功");
            response.put("data", data);

            String responseTopic = PREFIX + "/up/sub-devices/services/" + serviceCode + "/response";
            publish(responseTopic, response);
            System.out.println("[网关] 子设备服务响应已发送: serviceCode=" + serviceCode + ", msgId=" + msgId);
        } catch (Exception e) {
            System.err.println("[网关] 处理子设备服务请求异常: " + e.getMessage());
        }
    }

    /**
     * 处理子设备属性设置请求，逐属性回复 results。
     */
    @SuppressWarnings("unchecked")
    private void handlePropertySetRequest(String payload) {
        try {
            if (payload.contains("不回复")) {
                System.out.println("[网关] 收到「不回复」指令，跳过响应以模拟超时");
                return;
            }

            Map<String, Object> request = ObjectMapperUtil.readValue(payload, Map.class);
            String msgId = (String) request.get("msgId");
            Map<String, Object> target = (Map<String, Object>) request.getOrDefault("target", Map.of());
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
            response.put("target", target);
            response.put("code", 20000);
            response.put("message", "成功");
            response.put("results", results);

            publish(PREFIX + "/up/sub-devices/properties/set/response", response);
            System.out.println("[网关] 子设备属性设置响应已发送: msgId=" + msgId);
        } catch (Exception e) {
            System.err.println("[网关] 处理子设备属性设置异常: " + e.getMessage());
        }
    }

    /**
     * 处理拓扑同步请求，回复当前管理的子设备列表。
     */
    @SuppressWarnings("unchecked")
    private void handleTopologySyncRequest(String payload) {
        try {
            Map<String, Object> request = ObjectMapperUtil.readValue(payload, Map.class);
            String msgId = (String) request.get("msgId");

            Map<String, Object> device = new LinkedHashMap<>();
            device.put("productKey", SUB_PRODUCT_KEY);
            device.put("deviceCode", SUB_DEVICE_CODE);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("msgId", msgId);
            response.put("timestamp", Instant.now().toEpochMilli());
            response.put("code", 20000);
            response.put("message", "成功");
            response.put("devices", List.of(device));

            publish(PREFIX + "/up/sub-devices/topology/sync/response", response);
            System.out.println("[网关] 拓扑同步响应已发送: msgId=" + msgId);
        } catch (Exception e) {
            System.err.println("[网关] 处理拓扑同步请求异常: " + e.getMessage());
        }
    }

    /**
     * 上报子设备状态变更（DELTA 模式）。
     */
    private void reportSubDeviceStatus(String status) {
        try {
            Map<String, Object> device = new LinkedHashMap<>();
            device.put("productKey", SUB_PRODUCT_KEY);
            device.put("deviceCode", SUB_DEVICE_CODE);
            device.put("status", status);
            device.put("occurredAt", Instant.now().toEpochMilli());

            Map<String, Object> report = new LinkedHashMap<>();
            report.put("msgId", String.valueOf(msgIdGen.incrementAndGet()));
            report.put("mode", "DELTA");
            report.put("timestamp", Instant.now().toEpochMilli());
            report.put("devices", List.of(device));

            publish(PREFIX + "/up/sub-devices/status/report", report);
            System.out.println("[网关] 子设备状态已上报: " + SUB_PRODUCT_KEY + "/" + SUB_DEVICE_CODE + " -> " + status);
        } catch (Exception e) {
            System.err.println("[网关] 子设备状态上报异常: " + e.getMessage());
        }
    }

    /**
     * 定时批量上报子设备属性：temperature、humidity。
     */
    private void reportSubDeviceProperties() {
        try {
            Map<String, Object> properties = new LinkedHashMap<>();
            properties.put("temperature", Math.round((20 + ThreadLocalRandom.current().nextDouble(0, 10)) * 10.0) / 10.0);
            properties.put("humidity", Math.round((40 + ThreadLocalRandom.current().nextDouble(0, 30)) * 10.0) / 10.0);

            Map<String, Object> device = new LinkedHashMap<>();
            device.put("productKey", SUB_PRODUCT_KEY);
            device.put("deviceCode", SUB_DEVICE_CODE);
            device.put("occurredAt", Instant.now().toEpochMilli());
            device.put("properties", properties);

            Map<String, Object> report = new LinkedHashMap<>();
            report.put("msgId", String.valueOf(msgIdGen.incrementAndGet()));
            report.put("timestamp", Instant.now().toEpochMilli());
            report.put("devices", List.of(device));

            publish(PREFIX + "/up/sub-devices/properties/report", report);
            System.out.println("[网关] 子设备属性已上报: " + properties);
        } catch (Exception e) {
            System.err.println("[网关] 子设备属性上报异常: " + e.getMessage());
        }
    }

    /**
     * 定时上报子设备事件：temperature-change。
     */
    private void reportSubDeviceEvent() {
        try {
            Map<String, Object> source = new LinkedHashMap<>();
            source.put("productKey", SUB_PRODUCT_KEY);
            source.put("deviceCode", SUB_DEVICE_CODE);

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("temperature", Math.round((20 + ThreadLocalRandom.current().nextDouble(0, 15)) * 10.0) / 10.0);
            data.put("level", "WARNING");

            Map<String, Object> event = new LinkedHashMap<>();
            event.put("msgId", String.valueOf(msgIdGen.incrementAndGet()));
            event.put("timestamp", Instant.now().toEpochMilli());
            event.put("source", source);
            event.put("data", data);

            publish(PREFIX + "/up/sub-devices/events/temperature-change/report", event);
            System.out.println("[网关] 子设备事件已上报: temperature-change");
        } catch (Exception e) {
            System.err.println("[网关] 子设备事件上报异常: " + e.getMessage());
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
        reportSubDeviceStatus("OFFLINE");
        scheduler.shutdown();
        client.disconnect();
        System.out.println("[网关] " + GATEWAY_ID + " 已断开");
    }
}
