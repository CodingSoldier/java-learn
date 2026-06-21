# 设备直连与网关连接 Codex 审核

## 审核结论

服务调用主链路设计正确：HTTP 请求由平台生成字符串 `msgId`，根据设备路由发布到直连设备或网关 Topic；平台订阅服务响应，通过 Topic 身份、网关子设备身份和 pending 预期值完成原 `DeferredResult<ResponseEntity<?>>`。

本次审核发现并修复了测试 Client ID 冲突、Topic 解析过宽、响应 payload 校验不足、失败响应丢失完整消息等问题。不恢复 `/mock/mqtt/invoke-reply`，真实 MQTT 是唯一端到端测试路径。

## 修复内容

1. `EndToEndInvokeTest` 为平台客户端和模拟设备客户端增加同一次运行共享的随机标识，避免与已启动服务或并发测试互相踢下线。
2. Topic 解析改为精确匹配层级和方向：`up` 只接受 `response/report`，`down` 只接受 `request`；多余层级、未知业务路径和方向错配全部返回 `valid=false`。
3. 服务响应要求 `msgId` 为非空字符串、`timestamp` 为正整数、`code` 为整数。`data` 缺省时归一化为空 Map，类型不是 JSON Object 时拒绝。
4. 网关服务响应要求 `target.productKey` 和 `target.deviceCode` 为合法非空字符串，并继续与 pending 中的网关和子设备身份进行比对。
5. 响应时间与平台时间偏差超过 5 分钟时记录中文警告，但不影响当前 pending 的业务关联。
6. 合法的非 `20000` 响应使用完整 `ServiceResponseMessage` 完成 pending，由 `ServiceInvokeService` 统一转换为 HTTP Status 和 `Result.fail(code, message)`。
7. 端到端测试只保留直连调用、网关调用、设备失败、设备不存在和等待超时。属性、事件、状态及拓扑当前只测试 Topic 构建和解析，不宣称已实现平台处理链。
8. 删除旧 `/sys/servie/...` 主题常量，并更新 `AGENTS.md` 的架构说明。

## 验收标准

- `ServiceInvokeController.invoke` 和 `ServiceInvokeService.invoke` 继续返回 `DeferredResult<ResponseEntity<?>>`。
- 直连和网关服务调用成功时返回 HTTP 200 与完整嵌套 `data`。
- 设备业务失败、设备不存在、MQTT 发布失败和 30 秒超时保持现有响应协议。
- 非法 Topic 或 payload 不得完成或失败 pending，原请求继续等待合法响应或超时。
- 执行 `mvn clean test` 时全部测试通过。
- IoT 服务运行期间执行测试，不得因 MQTT Client ID 重复导致服务或测试客户端掉线。

## 后续范围

属性设置、属性上报、事件上报、状态同步、拓扑同步、跨节点 pending 路由和消息幂等存储仍按主设计文档后续阶段实施。
