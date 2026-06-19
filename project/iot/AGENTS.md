# Repository Guidelines

## Project Structure & Module Organization

This repository is a Maven-based Spring Boot IoT service. Main code lives under `src/main/java/com/example/iot`.

- `controller/`: HTTP entry points such as `/service/invoke` and mock MQTT reply APIs.
- `service/`: async request orchestration and pending request registry.
- `mqtt/`: MQTT gateway abstraction, HiveMQ Client implementation, and local pseudo gateway.
- `model/`: request, response, and MQTT payload DTOs.
- `config/`: configuration properties and MQTT client beans.
- `src/main/resources/application.yml`: runtime configuration.
- `src/test/java`: unit and MVC integration tests.

Do not commit generated build output from `target/` or IDE-only files unless intentionally required.

## Build, Test, and Development Commands

- `mvn test`: compiles the project and runs all tests.
- `mvn package`: builds the executable jar after running tests.
- `mvn spring-boot:run`: starts the service locally on port `8080`.

The project depends on the local `micro-service` framework artifacts. If Maven cannot resolve `com.github.codingsoldier:*:25.0.0`, run `mvn install` in `E:\github\micro-service` first.

By default the app connects to EMQX at `192.168.1.221:1883` using HiveMQ Client.

## Coding Style & Naming Conventions

Use Java 25 and Spring Boot conventions. Keep indentation at 4 spaces. Use Lombok where already used to reduce boilerplate. Public classes and methods should have concise JavaDoc in Chinese.

DTOs and response models should implement `Serializable` and define `serialVersionUID`. Prefer framework types from `micro-service-common`, especially `Result`, `HttpStatus4xxException`, and `HttpStatus5xxException`, instead of creating local duplicates.

## Testing Guidelines

Tests use JUnit 5, AssertJ, Spring MVC Test, and Mockito. Test classes should follow `*Test` naming, for example `PendingRequestRegistryTest`.

Cover async behavior explicitly: successful reply matching, timeout, unknown `msgId`, duplicate replies, pending request limits, MQTT publish payloads, and MQTT reply parsing. MVC tests should mock `MqttGateway` unless they intentionally require a real broker. Run `mvn test` before submitting changes.

## Commit & Pull Request Guidelines

Recent commits use short Chinese summaries, for example `第一版，服务调用`. Keep commit messages concise and action-oriented.

Pull requests should include a short description, affected APIs, test results, and any configuration changes. For API behavior changes, include example request and response JSON.

## Architecture Notes

`/service/invoke` intentionally returns `DeferredResult<ResponseEntity<?>>` to preserve async HTTP behavior. Keep this signature unless the async design is being intentionally redesigned. Response bodies should use `Result.success(...)` or `Result.fail(...)` for consistency with the `micro-service` framework.

The production MQTT path uses HiveMQ Client with MQTT 5. It publishes to `/sys/servie/invoke` and subscribes to `/sys/servie/invoke_reply`; keep the current topic spelling unless a coordinated migration changes both sides.
