package com.example.iot;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.iot.config.MqttProperties;
import com.example.iot.mqtt.MqttTopicResolver;
import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * 使用真实 EMQX 和 MQTT 客户端模拟设备的服务调用端到端测试。
 * <p>
 * 前置条件：EMQX broker 已在 192.168.1.221:1883 启动。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "iot.invoke.timeout=10s",
        "iot.invoke.max-pending=100"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EndToEndInvokeTest {

    private static final String TEST_RUN_ID = UUID.randomUUID().toString().replace("-", "");

    @LocalServerPort
    private int port;

    @Autowired
    private MqttProperties mqttProperties;

    private Mqtt5AsyncClient deviceClient;

    /**
     * 为测试中的平台 MQTT 客户端分配唯一 Client ID。
     *
     * @param registry 动态属性注册器
     */
    @DynamicPropertySource
    static void registerMqttClientId(DynamicPropertyRegistry registry) {
        registry.add("iot.mqtt.client-id", () -> "iot-e2e-platform-" + TEST_RUN_ID);
    }

    /**
     * 每个测试结束后断开模拟设备连接。
     *
     * @throws Exception MQTT 断开失败时抛出
     */
    @AfterEach
    void tearDown() throws Exception {
        if (deviceClient != null) {
            deviceClient.disconnect()
                    .orTimeout(5, TimeUnit.SECONDS)
                    .get(5, TimeUnit.SECONDS);
        }
    }

    /**
     * 验证直连设备服务调用完整链路。
     *
     * @throws Exception MQTT 或 HTTP 调用失败时抛出
     */
    @Test
    @Order(1)
    void directDeviceServiceInvoke() throws Exception {
        deviceClient = createDeviceClient("direct-service");
        String productKey = "light";
        String deviceCode = "light001";
        String serviceCode = "switch";
        CompletableFuture<Map<String, Object>> deviceReceived = new CompletableFuture<>();

        deviceClient.subscribe(buildSubscribe(MqttTopicResolver.directDownFilter(productKey, deviceCode)), publish -> {
            Map<String, Object> request = parsePayload(publish);
            String msgId = (String) request.get("msgId");
            Map<String, Object> response = serviceResponse(msgId, Map.of("value", true));
            publishJson(MqttTopicResolver.directServiceResponse(productKey, deviceCode, serviceCode), response);
            deviceReceived.complete(request);
        }).get(5, TimeUnit.SECONDS);

        ResponseEntity<String> response = new RestTemplate().postForEntity(
                url(), jsonEntity(serviceRequest(productKey, deviceCode, serviceCode)), String.class);

        assertSuccessfulHttpResponse(response);
        Map<String, Object> mqttRequest = deviceReceived.get(5, TimeUnit.SECONDS);
        assertThat(mqttRequest.get("msgId")).isInstanceOf(String.class);
        assertThat((String) mqttRequest.get("msgId")).matches("\\d+");
        assertThat(mapValue(mqttRequest, "data")).containsEntry("value", true);
    }

    /**
     * 验证网关子设备服务调用完整链路。
     *
     * @throws Exception MQTT 或 HTTP 调用失败时抛出
     */
    @Test
    @Order(2)
    void gatewaySubDeviceServiceInvoke() throws Exception {
        deviceClient = createDeviceClient("gateway-service");
        String gatewayId = "gw001";
        String productKey = "sensor";
        String deviceCode = "sensor001";
        String serviceCode = "switch";
        CompletableFuture<Map<String, Object>> gatewayReceived = new CompletableFuture<>();

        deviceClient.subscribe(buildSubscribe(MqttTopicResolver.gatewayDownFilter(gatewayId)), publish -> {
            Map<String, Object> request = parsePayload(publish);
            String msgId = (String) request.get("msgId");
            Map<String, Object> response = serviceResponse(msgId, Map.of("value", true));
            response.put("target", request.get("target"));
            publishJson(MqttTopicResolver.gatewayServiceResponse(gatewayId, serviceCode), response);
            gatewayReceived.complete(request);
        }).get(5, TimeUnit.SECONDS);

        ResponseEntity<String> response = new RestTemplate().postForEntity(
                url(), jsonEntity(serviceRequest(productKey, deviceCode, serviceCode)), String.class);

        assertSuccessfulHttpResponse(response);
        Map<String, Object> mqttRequest = gatewayReceived.get(5, TimeUnit.SECONDS);
        assertThat(mqttRequest.get("msgId")).isInstanceOf(String.class);
        Map<String, Object> target = mapValue(mqttRequest, "target");
        assertThat(target).containsEntry("productKey", productKey).containsEntry("deviceCode", deviceCode);
    }

    /**
     * 验证设备业务失败响应转换为 HTTP 失败结果。
     *
     * @throws Exception MQTT 或 HTTP 调用失败时抛出
     */
    @Test
    @Order(3)
    void deviceFailureResponse() throws Exception {
        deviceClient = createDeviceClient("direct-failure");
        String productKey = "light";
        String deviceCode = "light001";
        String serviceCode = "switch";

        deviceClient.subscribe(buildSubscribe(MqttTopicResolver.directDownFilter(productKey, deviceCode)), publish -> {
            Map<String, Object> request = parsePayload(publish);
            Map<String, Object> response = serviceResponse((String) request.get("msgId"), Map.of());
            response.put("code", 50000);
            response.put("message", "设备内部错误");
            publishJson(MqttTopicResolver.directServiceResponse(productKey, deviceCode, serviceCode), response);
        }).get(5, TimeUnit.SECONDS);

        ResponseEntity<String> response = createRestTemplateAcceptingAllStatus().postForEntity(
                url(), jsonEntity(serviceRequest(productKey, deviceCode, serviceCode)), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        Map<String, Object> body = parseJson(response.getBody());
        assertThat(body).containsEntry("code", 50000).containsEntry("message", "设备内部错误");
    }

    /**
     * 验证未配置设备返回 404。
     */
    @Test
    @Order(4)
    void deviceNotFound() {
        ResponseEntity<String> response = createRestTemplateAcceptingAllStatus().postForEntity(
                url(), jsonEntity(serviceRequest("nonexistent", "nonexistent", "switch")), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(parseJson(response.getBody())).containsEntry("code", 40400);
    }

    /**
     * 验证设备不回复时返回 504。
     */
    @Test
    @Order(5)
    void serviceInvokeTimeout() {
        ResponseEntity<String> response = createRestTemplateAcceptingAllStatus().postForEntity(
                url(), jsonEntity(serviceRequest("light", "light001", "switch")), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(504);
        Map<String, Object> body = parseJson(response.getBody());
        assertThat(body).containsEntry("code", 50400).containsEntry("message", "等待 MQTT 回复超时");
    }

    private Mqtt5AsyncClient createDeviceClient(String role) throws Exception {
        Mqtt5AsyncClient client = MqttClient.builder()
                .useMqttVersion5()
                .identifier("iot-e2e-" + role + "-" + TEST_RUN_ID)
                .serverHost(mqttProperties.getHost())
                .serverPort(mqttProperties.getPort())
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        client.connect(Mqtt5Connect.builder().keepAlive(60).cleanStart(true).build())
                .orTimeout(10, TimeUnit.SECONDS)
                .get(10, TimeUnit.SECONDS);
        return client;
    }

    private Map<String, Object> serviceRequest(String productKey, String deviceCode, String serviceCode) {
        return Map.of(
                "productKey", productKey,
                "deviceCode", deviceCode,
                "serviceCode", serviceCode,
                "data", Map.of("value", true));
    }

    private Map<String, Object> serviceResponse(String msgId, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("msgId", msgId);
        response.put("timestamp", System.currentTimeMillis());
        response.put("code", 20000);
        response.put("message", "成功");
        response.put("data", data);
        return response;
    }

    private void assertSuccessfulHttpResponse(ResponseEntity<String> response) {
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        Map<String, Object> body = parseJson(response.getBody());
        assertThat(body).containsEntry("code", 20000);
        assertThat(mapValue(mapValue(body, "data"), "data")).containsEntry("value", true);
    }

    private String url() {
        return "http://localhost:" + port + "/service/invoke";
    }

    private HttpEntity<Map<String, Object>> jsonEntity(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private RestTemplate createRestTemplateAcceptingAllStatus() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new org.springframework.web.client.DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(@org.springframework.lang.NonNull org.springframework.http.client.ClientHttpResponse response) {
                return false;
            }
        });
        return restTemplate;
    }

    private Mqtt5Subscribe buildSubscribe(String topicFilter) {
        return Mqtt5Subscribe.builder()
                .addSubscription()
                .topicFilter(topicFilter)
                .qos(MqttQos.AT_LEAST_ONCE)
                .applySubscription()
                .build();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parsePayload(Mqtt5Publish publish) {
        return ObjectMapperUtil.readValue(publish.getPayloadAsBytes(), Map.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        return ObjectMapperUtil.readValue(json, Map.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapValue(Map<String, Object> source, String key) {
        return (Map<String, Object>) source.get(key);
    }

    private void publishJson(String topic, Map<String, Object> data) {
        deviceClient.publish(Mqtt5Publish.builder()
                .topic(topic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(ObjectMapperUtil.writeValueAsString(data).getBytes(StandardCharsets.UTF_8))
                .build());
    }
}
