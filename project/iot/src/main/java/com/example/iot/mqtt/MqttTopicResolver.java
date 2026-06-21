package com.example.iot.mqtt;

import com.example.iot.model.MqttTopicMetadata;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * MQTT Topic 构建、解析和变量校验工具。
 * <p>
 * 主题格式：{@code iot/v1/{连接对象}/{对象标识}/{方向}/{业务类型}}
 */
@Slf4j
public final class MqttTopicResolver {

    private static final String PROTOCOL_PREFIX = "iot/v1";

    private static final String PRODUCTS = "products";
    private static final String DEVICES = "devices";
    private static final String GATEWAYS = "gateways";
    private static final String SUB_DEVICES = "sub-devices";

    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String SERVICES = "services";
    private static final String PROPERTIES = "properties";
    private static final String EVENTS = "events";
    private static final String STATUS = "status";
    private static final String TOPOLOGY = "topology";

    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";
    private static final String REPORT = "report";
    private static final String SET = "set";
    private static final String SYNC = "sync";

    private static final Pattern TOPIC_VAR_PATTERN = Pattern.compile("^[a-zA-Z0-9._\\-]{1,64}$");

    private static final String DIRECT_DEVICE = "DIRECT_DEVICE";
    private static final String GATEWAY_SUB_DEVICE = "GATEWAY_SUB_DEVICE";

    private MqttTopicResolver() {
    }

    // ========== 直连设备 Topic 构建 ==========

    /**
     * 构建直连设备服务请求 Topic。
     *
     * @param productKey  产品标识
     * @param deviceCode  设备编码
     * @param serviceCode 服务编码
     * @return 完整 Topic
     */
    public static String directServiceRequest(String productKey, String deviceCode, String serviceCode) {
        validateVariable(productKey, "productKey");
        validateVariable(deviceCode, "deviceCode");
        validateVariable(serviceCode, "serviceCode");
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/" + productKey + "/" + DEVICES + "/" + deviceCode
                + "/" + DOWN + "/" + SERVICES + "/" + serviceCode + "/" + REQUEST;
    }

    /**
     * 构建直连设备服务响应 Topic。
     *
     * @param productKey  产品标识
     * @param deviceCode  设备编码
     * @param serviceCode 服务编码
     * @return 完整 Topic
     */
    public static String directServiceResponse(String productKey, String deviceCode, String serviceCode) {
        validateVariable(productKey, "productKey");
        validateVariable(deviceCode, "deviceCode");
        validateVariable(serviceCode, "serviceCode");
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/" + productKey + "/" + DEVICES + "/" + deviceCode
                + "/" + UP + "/" + SERVICES + "/" + serviceCode + "/" + RESPONSE;
    }

    /**
     * 返回直连设备服务响应通配符过滤器。
     *
     * @return 通配符 Topic
     */
    public static String directServiceResponseFilter() {
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/+/" + DEVICES + "/+/" + UP + "/" + SERVICES + "/+/" + RESPONSE;
    }

    /**
     * 返回直连设备下行通配符过滤器。
     *
     * @param productKey 产品标识
     * @param deviceCode 设备编码
     * @return 通配符 Topic
     */
    public static String directDownFilter(String productKey, String deviceCode) {
        validateVariable(productKey, "productKey");
        validateVariable(deviceCode, "deviceCode");
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/" + productKey + "/" + DEVICES + "/" + deviceCode + "/" + DOWN + "/#";
    }

    /**
     * 构建直连设备属性上报 Topic。
     *
     * @param productKey 产品标识
     * @param deviceCode 设备编码
     * @return 完整 Topic
     */
    public static String directPropertyReport(String productKey, String deviceCode) {
        validateVariable(productKey, "productKey");
        validateVariable(deviceCode, "deviceCode");
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/" + productKey + "/" + DEVICES + "/" + deviceCode
                + "/" + UP + "/" + PROPERTIES + "/" + REPORT;
    }

    /**
     * 构建直连设备事件上报 Topic。
     *
     * @param productKey 产品标识
     * @param deviceCode 设备编码
     * @param eventCode  事件编码
     * @return 完整 Topic
     */
    public static String directEventReport(String productKey, String deviceCode, String eventCode) {
        validateVariable(productKey, "productKey");
        validateVariable(deviceCode, "deviceCode");
        validateVariable(eventCode, "eventCode");
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/" + productKey + "/" + DEVICES + "/" + deviceCode
                + "/" + UP + "/" + EVENTS + "/" + eventCode + "/" + REPORT;
    }

    /**
     * 构建直连设备属性设置请求 Topic。
     *
     * @param productKey 产品标识
     * @param deviceCode 设备编码
     * @return 完整 Topic
     */
    public static String directPropertySetRequest(String productKey, String deviceCode) {
        validateVariable(productKey, "productKey");
        validateVariable(deviceCode, "deviceCode");
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/" + productKey + "/" + DEVICES + "/" + deviceCode
                + "/" + DOWN + "/" + PROPERTIES + "/" + SET + "/" + REQUEST;
    }

    /**
     * 构建直连设备属性设置响应 Topic。
     *
     * @param productKey 产品标识
     * @param deviceCode 设备编码
     * @return 完整 Topic
     */
    public static String directPropertySetResponse(String productKey, String deviceCode) {
        validateVariable(productKey, "productKey");
        validateVariable(deviceCode, "deviceCode");
        return PROTOCOL_PREFIX + "/" + PRODUCTS + "/" + productKey + "/" + DEVICES + "/" + deviceCode
                + "/" + UP + "/" + PROPERTIES + "/" + SET + "/" + RESPONSE;
    }

    // ========== 网关 Topic 构建 ==========

    /**
     * 构建网关子设备服务请求 Topic。
     *
     * @param gatewayId   网关标识
     * @param serviceCode 服务编码
     * @return 完整 Topic
     */
    public static String gatewayServiceRequest(String gatewayId, String serviceCode) {
        validateVariable(gatewayId, "gatewayId");
        validateVariable(serviceCode, "serviceCode");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + DOWN + "/" + SUB_DEVICES + "/" + SERVICES + "/" + serviceCode + "/" + REQUEST;
    }

    /**
     * 构建网关子设备服务响应 Topic。
     *
     * @param gatewayId   网关标识
     * @param serviceCode 服务编码
     * @return 完整 Topic
     */
    public static String gatewayServiceResponse(String gatewayId, String serviceCode) {
        validateVariable(gatewayId, "gatewayId");
        validateVariable(serviceCode, "serviceCode");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + UP + "/" + SUB_DEVICES + "/" + SERVICES + "/" + serviceCode + "/" + RESPONSE;
    }

    /**
     * 返回网关子设备服务响应通配符过滤器。
     *
     * @return 通配符 Topic
     */
    public static String gatewayServiceResponseFilter() {
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/+/" + UP + "/" + SUB_DEVICES + "/" + SERVICES + "/+/" + RESPONSE;
    }

    /**
     * 返回网关下行通配符过滤器。
     *
     * @param gatewayId 网关标识
     * @return 通配符 Topic
     */
    public static String gatewayDownFilter(String gatewayId) {
        validateVariable(gatewayId, "gatewayId");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId + "/" + DOWN + "/#";
    }

    /**
     * 构建网关子设备属性上报 Topic。
     *
     * @param gatewayId 网关标识
     * @return 完整 Topic
     */
    public static String gatewayPropertyReport(String gatewayId) {
        validateVariable(gatewayId, "gatewayId");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + UP + "/" + SUB_DEVICES + "/" + PROPERTIES + "/" + REPORT;
    }

    /**
     * 构建网关子设备事件上报 Topic。
     *
     * @param gatewayId 网关标识
     * @param eventCode 事件编码
     * @return 完整 Topic
     */
    public static String gatewayEventReport(String gatewayId, String eventCode) {
        validateVariable(gatewayId, "gatewayId");
        validateVariable(eventCode, "eventCode");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + UP + "/" + SUB_DEVICES + "/" + EVENTS + "/" + eventCode + "/" + REPORT;
    }

    /**
     * 构建网关子设备状态上报 Topic。
     *
     * @param gatewayId 网关标识
     * @return 完整 Topic
     */
    public static String gatewayStatusReport(String gatewayId) {
        validateVariable(gatewayId, "gatewayId");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + UP + "/" + SUB_DEVICES + "/" + STATUS + "/" + REPORT;
    }

    /**
     * 构建网关子设备属性设置请求 Topic。
     *
     * @param gatewayId 网关标识
     * @return 完整 Topic
     */
    public static String gatewayPropertySetRequest(String gatewayId) {
        validateVariable(gatewayId, "gatewayId");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + DOWN + "/" + SUB_DEVICES + "/" + PROPERTIES + "/" + SET + "/" + REQUEST;
    }

    /**
     * 构建网关子设备属性设置响应 Topic。
     *
     * @param gatewayId 网关标识
     * @return 完整 Topic
     */
    public static String gatewayPropertySetResponse(String gatewayId) {
        validateVariable(gatewayId, "gatewayId");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + UP + "/" + SUB_DEVICES + "/" + PROPERTIES + "/" + SET + "/" + RESPONSE;
    }

    /**
     * 构建网关子设备拓扑同步请求 Topic。
     *
     * @param gatewayId 网关标识
     * @return 完整 Topic
     */
    public static String gatewayTopologySyncRequest(String gatewayId) {
        validateVariable(gatewayId, "gatewayId");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + DOWN + "/" + SUB_DEVICES + "/" + TOPOLOGY + "/" + SYNC + "/" + REQUEST;
    }

    /**
     * 构建网关子设备拓扑同步响应 Topic。
     *
     * @param gatewayId 网关标识
     * @return 完整 Topic
     */
    public static String gatewayTopologySyncResponse(String gatewayId) {
        validateVariable(gatewayId, "gatewayId");
        return PROTOCOL_PREFIX + "/" + GATEWAYS + "/" + gatewayId
                + "/" + UP + "/" + SUB_DEVICES + "/" + TOPOLOGY + "/" + SYNC + "/" + RESPONSE;
    }

    // ========== Topic 解析 ==========

    /**
     * 解析 MQTT Topic 为元数据。
     *
     * @param topic 完整 Topic 字符串
     * @return 解析结果，解析失败时 valid=false
     */
    public static MqttTopicMetadata parse(String topic) {
        if (topic == null || topic.isEmpty()) {
            log.warn("Topic 为空，无法解析");
            return MqttTopicMetadata.builder().valid(false).build();
        }

        String[] parts = topic.split("/", -1);
        if (parts.length < 2 || !PROTOCOL_PREFIX.equals(parts[0] + "/" + parts[1])) {
            log.warn("Topic 前缀不合法，topic={}", topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        if (parts.length < 3) {
            log.warn("Topic 层级不足，topic={}", topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        String objectType = parts[2];
        if (PRODUCTS.equals(objectType)) {
            return parseDirectTopic(parts, topic);
        } else if (GATEWAYS.equals(objectType)) {
            return parseGatewayTopic(parts, topic);
        } else {
            log.warn("Topic 连接对象类型不合法，objectType={}，topic={}", objectType, topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }
    }

    // ========== 内部方法 ==========

    private static MqttTopicMetadata parseDirectTopic(String[] parts, String topic) {
        // iot/v1/products/{productKey}/devices/{deviceCode}/...
        // 0     1  2        3           4      5          6 ...
        if (parts.length < 9) {
            log.warn("直连设备 Topic 层级不足，topic={}", topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }
        if (!PRODUCTS.equals(parts[2]) || !DEVICES.equals(parts[4])) {
            log.warn("直连设备 Topic 固定路径段不匹配，topic={}", topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        String productKey = parts[3];
        String deviceCode = parts[5];
        if (!isValidTopicVar(productKey) || !isValidTopicVar(deviceCode)) {
            log.warn("直连设备 Topic 变量不合法，productKey={}，deviceCode={}，topic={}", productKey, deviceCode, topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        String direction = parts[6];
        if (!UP.equals(direction) && !DOWN.equals(direction)) {
            log.warn("直连设备 Topic 方向不合法，direction={}，topic={}", direction, topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        MqttTopicMetadata.MqttTopicMetadataBuilder builder = MqttTopicMetadata.builder()
                .accessType(DIRECT_DEVICE)
                .direction(direction)
                .productKey(productKey)
                .deviceCode(deviceCode);

        if (SERVICES.equals(parts[7]) && parts.length == 10) {
            String serviceCode = parts[8];
            if (!isValidTopicVar(serviceCode)) {
                log.warn("服务编码不合法，serviceCode={}，topic={}", serviceCode, topic);
                return MqttTopicMetadata.builder().valid(false).build();
            }
            builder.serviceCode(serviceCode);
            if (UP.equals(direction) && RESPONSE.equals(parts[9])) {
                builder.messageType("services-response");
            } else if (DOWN.equals(direction) && REQUEST.equals(parts[9])) {
                builder.messageType("services-request");
            } else {
                return invalidTopic("直连设备服务 Topic 方向与消息类型不匹配", topic);
            }
        } else if (PROPERTIES.equals(parts[7]) && parts.length == 9
                && UP.equals(direction) && REPORT.equals(parts[8])) {
            builder.messageType("properties-report");
        } else if (PROPERTIES.equals(parts[7]) && parts.length == 10 && SET.equals(parts[8])) {
            if (UP.equals(direction) && RESPONSE.equals(parts[9])) {
                builder.messageType("properties-set-response");
            } else if (DOWN.equals(direction) && REQUEST.equals(parts[9])) {
                builder.messageType("properties-set-request");
            } else {
                return invalidTopic("直连设备属性设置 Topic 方向与消息类型不匹配", topic);
            }
        } else if (EVENTS.equals(parts[7]) && parts.length == 10
                && UP.equals(direction) && REPORT.equals(parts[9])) {
            String eventCode = parts[8];
            if (!isValidTopicVar(eventCode)) {
                log.warn("事件编码不合法，eventCode={}，topic={}", eventCode, topic);
                return MqttTopicMetadata.builder().valid(false).build();
            }
            builder.eventCode(eventCode);
            builder.messageType("events-report");
        } else {
            return invalidTopic("直连设备 Topic 业务路径不合法", topic);
        }

        return builder.valid(true).build();
    }

    private static MqttTopicMetadata parseGatewayTopic(String[] parts, String topic) {
        // iot/v1/gateways/{gatewayId}/...
        // 0     1  2        3          4 ...
        if (parts.length < 8) {
            log.warn("网关 Topic 层级不足，topic={}", topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }
        if (!GATEWAYS.equals(parts[2])) {
            log.warn("网关 Topic 固定路径段不匹配，topic={}", topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        String gatewayId = parts[3];
        if (!isValidTopicVar(gatewayId)) {
            log.warn("网关 Topic 变量不合法，gatewayId={}，topic={}", gatewayId, topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        String direction = parts[4];
        if (!UP.equals(direction) && !DOWN.equals(direction)) {
            log.warn("网关 Topic 方向不合法，direction={}，topic={}", direction, topic);
            return MqttTopicMetadata.builder().valid(false).build();
        }

        MqttTopicMetadata.MqttTopicMetadataBuilder builder = MqttTopicMetadata.builder()
                .accessType(GATEWAY_SUB_DEVICE)
                .direction(direction)
                .gatewayId(gatewayId);

        if (!SUB_DEVICES.equals(parts[5])) {
            return invalidTopic("网关 Topic 缺少 sub-devices 固定路径", topic);
        }

        if (SERVICES.equals(parts[6]) && parts.length == 9) {
            String serviceCode = parts[7];
            if (!isValidTopicVar(serviceCode)) {
                log.warn("服务编码不合法，serviceCode={}，topic={}", serviceCode, topic);
                return MqttTopicMetadata.builder().valid(false).build();
            }
            builder.serviceCode(serviceCode);
            if (UP.equals(direction) && RESPONSE.equals(parts[8])) {
                builder.messageType("services-response");
            } else if (DOWN.equals(direction) && REQUEST.equals(parts[8])) {
                builder.messageType("services-request");
            } else {
                return invalidTopic("网关服务 Topic 方向与消息类型不匹配", topic);
            }
        } else if (PROPERTIES.equals(parts[6]) && parts.length == 8
                && UP.equals(direction) && REPORT.equals(parts[7])) {
            builder.messageType("properties-report");
        } else if (PROPERTIES.equals(parts[6]) && parts.length == 9 && SET.equals(parts[7])) {
            if (UP.equals(direction) && RESPONSE.equals(parts[8])) {
                builder.messageType("properties-set-response");
            } else if (DOWN.equals(direction) && REQUEST.equals(parts[8])) {
                builder.messageType("properties-set-request");
            } else {
                return invalidTopic("网关属性设置 Topic 方向与消息类型不匹配", topic);
            }
        } else if (EVENTS.equals(parts[6]) && parts.length == 9
                && UP.equals(direction) && REPORT.equals(parts[8])) {
            String eventCode = parts[7];
            if (!isValidTopicVar(eventCode)) {
                log.warn("事件编码不合法，eventCode={}，topic={}", eventCode, topic);
                return MqttTopicMetadata.builder().valid(false).build();
            }
            builder.eventCode(eventCode).messageType("events-report");
        } else if (STATUS.equals(parts[6]) && parts.length == 8
                && UP.equals(direction) && REPORT.equals(parts[7])) {
            builder.messageType("status-report");
        } else if (TOPOLOGY.equals(parts[6]) && parts.length == 9 && SYNC.equals(parts[7])) {
            if (UP.equals(direction) && RESPONSE.equals(parts[8])) {
                builder.messageType("topology-sync-response");
            } else if (DOWN.equals(direction) && REQUEST.equals(parts[8])) {
                builder.messageType("topology-sync-request");
            } else {
                return invalidTopic("网关拓扑同步 Topic 方向与消息类型不匹配", topic);
            }
        } else {
            return invalidTopic("网关 Topic 业务路径不合法", topic);
        }

        return builder.valid(true).build();
    }

    private static MqttTopicMetadata invalidTopic(String reason, String topic) {
        log.warn("{}，topic={}", reason, topic);
        return MqttTopicMetadata.builder().valid(false).build();
    }

    /**
     * 校验 Topic 变量是否合法：1 至 64 字符，只允许字母、数字、点、下划线和连字符。
     *
     * @param value    变量值
     * @param variable 变量名，用于日志
     */
    public static void validateVariable(String value, String variable) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(variable + " 不能为空");
        }
        if (value.length() > 64) {
            throw new IllegalArgumentException(variable + " 长度不能超过 64，当前长度=" + value.length());
        }
        if (!TOPIC_VAR_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(variable + " 包含非法字符，只允许字母、数字、点、下划线和连字符，value=" + value);
        }
    }

    /**
     * 校验 Topic 变量是否合法。
     *
     * @param value 变量值
     * @return 是否合法
     */
    public static boolean isValidTopicVar(String value) {
        return value != null && TOPIC_VAR_PATTERN.matcher(value).matches();
    }
}
