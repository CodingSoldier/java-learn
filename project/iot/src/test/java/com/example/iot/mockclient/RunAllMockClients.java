package com.example.iot.mockclient;

import java.time.Duration;

/**
 * 启动所有模拟设备客户端。
 * <p>
 * 包含：
 * <ul>
 *   <li>直连设备：light/light001</li>
 *   <li>网关：gw001（代理子设备 sensor/sensor001）</li>
 * </ul>
 * <p>
 * 运行后持续 1 小时，期间设备会定时上报属性（每 1 分钟）和事件（每 5 分钟），
 * 并响应平台下发的服务调用和属性设置。
 * <p>
 * 如需模拟设备响应超时，平台下发的 payload 包含"不回复"即可。
 */
public class RunAllMockClients {

    private static final String HOST = "192.168.1.221";
    private static final int PORT = 1883;

    public static void main(String[] args) throws Exception {
        DirectDeviceClient directDevice = new DirectDeviceClient(HOST, PORT);
        GatewayClient gateway = new GatewayClient(HOST, PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("正在关闭模拟客户端...");
            directDevice.shutdown();
            gateway.shutdown();
        }));

        directDevice.start();
        gateway.start();

        System.out.println();
        System.out.println("========================================");
        System.out.println("所有模拟设备已启动");
        System.out.println("  直连设备: light/light001");
        System.out.println("  网关:     gw001 → 子设备 sensor/sensor001");
        System.out.println("  属性上报: 每 1 分钟");
        System.out.println("  事件上报: 每 5 分钟");
        System.out.println("  模拟超时: payload 包含「不回复」");
        System.out.println("========================================");
        System.out.println();

        Thread.sleep(Duration.ofHours(1));

        directDevice.shutdown();
        gateway.shutdown();
        System.out.println("模拟客户端已正常退出");
    }
}
