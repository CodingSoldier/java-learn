# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test

```bash
mvn test                          # compile + run all tests
mvn test -pl . -Dtest=HiveMqttGatewayTest   # single test class
mvn test -Dtest=HiveMqttGatewayTest#shouldPublishInvokeMessageAsJson  # single method
mvn package                       # build executable jar
mvn spring-boot:run               # start on port 8080
```

**Prerequisite**: This project depends on a local framework at `E:\github\micro-service`. If Maven cannot resolve `com.github.codingsoldier:*:25.0.0`, run `mvn install` in that directory first.

## Tech Stack

- **Java 25** with virtual threads enabled (`spring.threads.virtual.enabled=true`)
- **Spring Boot 4.0.6**
- **HiveMQ MQTT Client 1.3.15** (MQTT 5 async)
- **Lombok** for boilerplate reduction
- Local MQTT broker: EMQX 5.8.8 via `doc/docker-compose.yml`

## Architecture

### Dual Access Model

Two MQTT接入方式，上层业务不感知差异：

- **设备直连** (`DIRECT_DEVICE`)：设备独立连接 EMQX，Topic 含 `productKey` + `deviceCode`。
- **网关代理** (`GATEWAY_SUB_DEVICE`)：网关代理子设备，Topic 只含 `gatewayId`，payload 携带子设备身份。

`DeviceRouteResolver` 根据 `(productKey, deviceCode)` 解析接入路由（`AccessType` + `gatewayId`），决定下行 Topic 前缀。

### MQTT Topic 协议

```
sys/v1/{连接对象}/{对象标识}/{方向}/{业务类型}
```

- 直连：`sys/v1/products/{productKey}/devices/{deviceCode}/{up|down}/...`
- 网关：`sys/v1/gateways/{gatewayId}/{up|down}/sub-devices/...`
- 方向：`up`（设备→平台）、`down`（平台→设备）
- Topic 变量约束：1-64 字符，`[a-zA-Z0-9._-]`

`MqttTopicResolver` 负责 Topic 构建、解析和变量校验。

### Async HTTP-to-MQTT Bridge

```
HTTP POST /service/invoke
    → ServiceInvokeService.invoke()
        → DeviceRouteResolver.resolve()
        → MsgIdGenerator (snowflake, thread-safe)
        → PendingRequestRegistry.register()  (Semaphore-gated, max 10k pending)
        → MqttGateway.sendInvoke()           (publishes to resolved topic)
        → DeferredResult wired to CompletableFuture

MQTT reply (services-response)
    → HiveMqttUpstreamSubscriber
        → MqttUpstreamDispatcher.dispatch()
            → ServiceResponseHandler
                → PendingRequestRegistry.complete()
                    → DeferredResult.setResult()
```

Key classes:
- **`MqttGateway`** — interface: `HiveMqttGateway`（生产，MQTT 5）和 `PseudoMqttGateway`（桩，仅日志）
- **`DeviceRouteResolver` / `InMemoryDeviceRouteResolver`** — 设备路由解析，决定接入方式和下行 Topic
- **`MqttTopicResolver`** — Topic 构建、解析、变量校验（纯静态工具类）
- **`MqttUpstreamDispatcher`** — 上行消息分发，解析 Topic 元数据后路由到对应 Handler
- **`PendingRequestRegistry`** — `ConcurrentHashMap<String, PendingRequest>` + `Semaphore` 限流 + `ScheduledExecutorService` 超时
- **`ServiceInvokeService`** — 编排：路由解析 → msgId 生成 → pending 注册 → DeferredResult 绑定 → MQTT 发布
- **`MqttClientConfiguration`** — 创建 `Mqtt5AsyncClient` bean，支持自动重连

## Coding Conventions

- **Language**: JavaDoc and log messages in Chinese.
- **Indentation**: 4 spaces.
- **DTOs**: implement `Serializable`, define `serialVersionUID = 1L`, use Lombok `@Data @Builder @NoArgsConstructor @AllArgsConstructor`.
- **Framework types**: use `Result`, `HttpStatus4xxException`, `HttpStatus5xxException` from `micro-service-common` — do not create local duplicates.
- **Null/empty handling**: use `StringUtils` / `CollectionUtils` from common utils. Never return `null` for String or Collection results.
- **HiveMQ builders are immutable**: `builder.simpleAuth(...)` returns a new instance — always reassign (`builder = builder.simpleAuth(...)`), or the change is silently lost.

## Testing

- **JUnit 5 + AssertJ + Mockito**. Test classes named `*Test`.
- **`@MockitoBean`** (Spring Boot 4.x style) for mocking in MVC tests.
- **`@TestPropertySource`** for overriding config (e.g., shorter timeouts).
- MVC tests mock `MqttGateway` — no real broker needed.
- `MqttTopicResolverTest` 覆盖 Topic 构建、解析和变量校验。
- Async coverage expected: successful reply, timeout, unknown msgId, duplicate reply, max-pending rejection.

## Topic Examples

```
# 直连设备服务调用
sys/v1/products/{productKey}/devices/{deviceCode}/down/services/{serviceCode}/request
sys/v1/products/{productKey}/devices/{deviceCode}/up/services/{serviceCode}/response

# 网关子设备服务调用
sys/v1/gateways/{gatewayId}/down/sub-devices/services/{serviceCode}/request
sys/v1/gateways/{gatewayId}/up/sub-devices/services/{serviceCode}/response
```

`MqttTopicResolver` 提供完整的构建和解析方法，不要手动拼接 Topic 字符串。

## Configuration (`application.yml`)

| Property | Default | Description |
|---|---|---|
| `iot.invoke.timeout` | 30s | Per-request MQTT reply timeout |
| `iot.invoke.max-pending` | 10000 | Max concurrent in-flight requests |
| `iot.routing.devices[].product-key` | — | 设备产品标识 |
| `iot.routing.devices[].device-code` | — | 设备编码 |
| `iot.routing.devices[].access-type` | — | `DIRECT_DEVICE` 或 `GATEWAY_SUB_DEVICE` |
| `iot.routing.devices[].gateway-id` | — | 网关标识（仅网关代理模式） |
| `iot.mqtt.host` | 192.168.1.221 | EMQX broker host |
| `iot.mqtt.port` | 1883 | EMQX broker port |
| `iot.mqtt.qos` | 1 | MQTT QoS level |

## Commit Style

Short Chinese summaries, concise and action-oriented (e.g., `整合mqtt`, `第一版，服务调用`).
