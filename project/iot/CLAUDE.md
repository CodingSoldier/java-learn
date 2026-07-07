# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test

```bash
mvn test                          # compile + run all tests
mvn test -pl . -Dtest=MqttTopicResolverTest   # single test class
mvn test -Dtest=PendingRequestRegistryTest#someMethod  # single method
mvn package                       # build executable jar
mvn spring-boot:run               # start on port 8080
```

**Prerequisite**: This project depends on a local framework at `E:\github\micro-service`. If Maven cannot resolve `com.github.codingsoldier:*:25.0.0`, run `mvn install` in that directory first.

`EndToEndInvokeTest` 依赖**真实 EMQX broker**（`doc/中间件/docker-compose.yml`），并使用每次运行唯一的 Client ID 隔离连接；单元测试用 mock `Mqtt5AsyncClient`，不需要 broker。

## Tech Stack

- **Java 25** with virtual threads enabled (`spring.threads.virtual.enabled=true`)
- **Spring Boot 4.0.6**
- **HiveMQ MQTT Client 1.3.15** (MQTT 5 async)
- **Lombok** for boilerplate reduction
- Local MQTT broker: EMQX via `doc/中间件/docker-compose.yml`

## 设计文档

权威协议与实现规划在 `doc/设计文档/`，改动前先读：

- **`20260621-设备直连-网关连接.md`** — 主设计文档：双接入模型、`sys/v1` 主题协议、公共消息字段、业务结果码、分阶段实施计划（阶段一服务调用 → 阶段二属性/事件 → 阶段三状态/拓扑 → 阶段四生产接入）、ACL/安全、Codex 审核记录与阶段一验收标准。
- **`20260619-服务调用-上报-类关系图.md`** — 服务调用链路的类依赖图、时序图与各类职责边界。

**当前实现状态**：仅完成**阶段一服务调用闭环**（直连 + 网关）。属性设置、属性上报、事件、状态同步、拓扑同步当前**只实现 Topic 构建与解析**（`MqttTopicResolver`），尚无平台处理链；`MqttUpstreamDispatcher` 只分发 `services-response`。旧 `/sys/servie/...` 主题已删除，不做新旧双发。

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
        → MsgIdGenerator.nextId()             (snowflake → 十进制字符串 msgId)
        → 构造 DirectServiceRequestMessage / GatewayServiceRequestMessage
        → PendingRequestRegistry.register()    (Semaphore-gated, max 10k；保存预期身份 + serviceCode)
        → HiveMqttGateway.publish(MqttPublishRequest)  (发布到解析出的 down Topic)
        → DeferredResult wired to CompletableFuture<ServiceResponseMessage>

MQTT reply (services-response，up Topic)
    → HiveMqttUpstreamSubscriber               (订阅两个 services-response 通配符)
        → MqttUpstreamDispatcher.dispatch()    (解析 Topic + 严格校验 payload 字段)
            → ServiceResponseHandler.handle()  (校验 Topic/target/serviceCode 与 pending 预期一致)
                → PendingRequestRegistry.complete()
                    → DeferredResult.setResult()  (code=20000 → 200/Result.success；否则 Result.fail)
```

`correlationData` 固定为 msgId 的 UTF-8 字节；真正的请求关联以 payload 中的字符串 `msgId` 为准。

Key classes:
- **`ServiceInvokeService`** — 编排：路由解析 → msgId 生成 → 构造请求消息 → pending 注册 → 发布 → DeferredResult 绑定 → MQTT `code/message/data` 映射为 HTTP 结果（`CommonUtil.getResponseStatus(code)`）
- **`HiveMqttGateway`** — 唯一 MQTT 发布网关（`@Service`，无接口、无桩实现）。只负责序列化、设置 QoS / MQTT 5 属性并调用 HiveMQ Client，`publish(MqttPublishRequest)`。
- **`HiveMqttClientLifecycle`** — `ApplicationReadyEvent` 时 connect，再订阅两个服务响应过滤器；自动重连后恢复订阅；`@PreDestroy` 断开。
- **`HiveMqttUpstreamSubscriber`** — 订阅直连与网关 `services-response` 通配符，把原始 topic + payload 交给分发器。
- **`MqttUpstreamDispatcher`** — 上行分发 + 严格校验：`msgId` 非空字符串、`timestamp` 正整数、`code` 整数、`data` 必须是 JSON Object（缺省归一化为空 Map）、时钟偏差 >5 分钟告警；网关消息额外校验 payload 中 `target.productKey/deviceCode`。返回 `MqttDispatchResult(msgId, matched)`，不返回 null。
- **`ServiceResponseHandler`** — 身份校验：直连比对 Topic `productKey+deviceCode`，网关比对 Topic `gatewayId` + payload `target` 子设备身份，并校验 `serviceCode`，全部与 pending 预期一致才 complete。
- **`DeviceRouteResolver` / `InMemoryDeviceRouteResolver`** — 设备路由解析（接口 + 阶段一内存实现，读 `iot.routing.devices`），决定 `AccessType` + `gatewayId` 与下行 Topic。
- **`MqttTopicResolver`** — Topic 构建、解析、变量校验（纯静态工具类，精确匹配层级与方向）。
- **`PendingRequestRegistry`** — 唯一 pending 状态持有者：`ConcurrentHashMap<String, PendingRequest>` + `Semaphore` 限流 + 单线程 `ScheduledExecutorService` 超时。complete/fail/cancel/timeout 通过原子 `remove` 保证只有一条路径释放许可。**单机内存，不支持多节点。**
- **`MsgIdGenerator`** — 雪花 ID，`nextId()` 返回十进制字符串（msgId 全链路用 `String`）。
- **`MqttClientConfiguration`** — 创建 `Mqtt5AsyncClient` bean（`automaticReconnectWithDefaultConfig`）。

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
- 单元测试 mock `Mqtt5AsyncClient`（或上层组件），无需真实 broker；`EndToEndInvokeTest` 用真实 HiveMQ 客户端模拟直连设备/网关，依赖 EMQX。
- `MqttTopicResolverTest` 覆盖 Topic 构建、解析、变量校验（空/超长/含 `/ + #` 拒绝）。
- 覆盖期望：直连/网关成功调用、设备失败响应、设备不存在、未知 msgId、重复响应、身份/Topic 不匹配忽略、max-pending 拒绝、30s 超时（HTTP 504 + `Result.fail(50400, ...)`）、断线重连恢复订阅。
- Snowflake msgId 经 JSON 序列化/反序列化后必须与原值完全一致（超出 JS 安全整数范围仍以字符串保真）。

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

## 消息协议（payload）

UTF-8 JSON，公共字段：`msgId`（**String**，1-64，平台下行用 Snowflake 十进制字符串，禁止前端传入）、`timestamp`（Number，UTC 毫秒）、`code`（Number 业务码）、`message`（String）、`data`（**JSON Object → `Map<String,Object>`**，缺省归一化为空 `{}`，**绝不返回 `null`**）。网关请求/响应 payload 额外携带 `target.productKey/deviceCode` 定位子设备。

基础业务码：`20000` 成功 / `20700` 部分成功 / `40000` 参数非法 / `40400` 目标不存在 / `40900` 冲突或重复 / `42900` 限流 / `50000` 内部失败 / `50400` 超时。`code=20000` → HTTP 200 + `Result.success`；非 `20000` → `CommonUtil.getResponseStatus(code)` + `Result.fail(code, message)`。请求模型 `ServiceInvokeRequest`：`productKey`/`deviceCode`/`serviceCode` 必填，`data` 必填（可为空 Map）；`targetType`/`gatewayId`/`msgId` 由平台解析或生成，不接受前端传入。

## Configuration (`application.yml`)

| Property | Default | Description |
|---|---|---|
| `iot.invoke.timeout` | 30s | Per-request MQTT reply timeout |
| `iot.invoke.max-pending` | 10000 | Max concurrent in-flight requests |
| `iot.routing.devices[].product-key` | — | 设备产品标识 |
| `iot.routing.devices[].device-code` | — | 设备编码 |
| `iot.routing.devices[].access-type` | — | `DIRECT_DEVICE` 或 `GATEWAY_SUB_DEVICE` |
| `iot.routing.devices[].gateway-id` | — | 网关标识（仅网关代理模式，直连禁止配置；网关必填） |
| `iot.mqtt.host` | 192.168.1.221 | EMQX broker host |
| `iot.mqtt.port` | 1883 | EMQX broker port |
| `iot.mqtt.client-id` | iot-service | MQTT 客户端 ID |
| `iot.mqtt.username` / `password` | `""` | 为空时匿名连接 |
| `iot.mqtt.qos` | 1 | 发布/订阅 QoS 等级 |
| `iot.mqtt.keep-alive` | 60s | MQTT keep alive |
| `iot.mqtt.connect-timeout` | 10s | 初次连接超时 |

> `InMemoryDeviceRouteResolver` 启动时为 `(productKey, deviceCode)` 建唯一索引：重复配置、直连带 `gateway-id`、网关缺 `gateway-id` 都会导致启动失败。未知设备抛 `HttpStatus4xxException(40400)`，网关路由无效抛 `40900`。

## Commit Style

Short Chinese summaries, concise and action-oriented (e.g., `整合mqtt`, `第一版，服务调用`).
