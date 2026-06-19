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

## Architecture: Async HTTP-to-MQTT Bridge

The service accepts HTTP POST at `/service/invoke`, publishes to MQTT, and holds the HTTP response open via `DeferredResult` until an MQTT reply arrives or timeout fires.

```
HTTP POST /service/invoke
    → ServiceInvokeService.invoke()
        → MsgIdGenerator (AtomicLong, thread-safe)
        → PendingRequestRegistry.register()  (Semaphore-gated, max 10k pending)
        → MqttGateway.sendInvoke()           (publishes to /sys/servie/invoke)
        → DeferredResult wired to CompletableFuture

MQTT reply on /sys/servie/invoke_reply
    → HiveMqttGateway.handleReplyPayload()
        → MqttReplyHandler.completeReply()
            → PendingRequestRegistry.complete()
                → DeferredResult.setResult()
```

Key classes:
- **`MqttGateway`** — interface with two implementations: `HiveMqttGateway` (production, MQTT 5) and `PseudoMqttGateway` (stub, logs only)
- **`PendingRequestRegistry`** — in-memory `ConcurrentHashMap<Long, PendingRequest>` with `Semaphore` for backpressure and `ScheduledExecutorService` for per-request timeouts
- **`ServiceInvokeService`** — orchestration: msgId generation → pending registration → DeferredResult wiring → MQTT publish
- **`MqttClientConfiguration`** — creates `Mqtt5AsyncClient` bean with auto-reconnect

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
- Async coverage expected: successful reply, timeout, unknown msgId, duplicate reply, max-pending rejection.

## Topics (intentional spelling)

- Publish: `/sys/servie/invoke`
- Subscribe: `/sys/servie/invoke_reply`

The "servie" typo is known and preserved — both sides must match.

## Configuration (`application.yml`)

| Property | Default | Description |
|---|---|---|
| `iot.invoke.timeout` | 30s | Per-request MQTT reply timeout |
| `iot.invoke.max-pending` | 10000 | Max concurrent in-flight requests |
| `iot.mqtt.host` | 192.168.1.221 | EMQX broker host |
| `iot.mqtt.port` | 1883 | EMQX broker port |
| `iot.mqtt.qos` | 1 | MQTT QoS level |

## Commit Style

Short Chinese summaries, concise and action-oriented (e.g., `整合mqtt`, `第一版，服务调用`).
