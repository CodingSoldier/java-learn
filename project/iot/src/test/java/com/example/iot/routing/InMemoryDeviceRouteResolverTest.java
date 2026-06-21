package com.example.iot.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.iot.config.DeviceRouteProperties;
import com.example.iot.config.DeviceRouteProperties.DeviceRouteConfig;
import com.github.codingsoldier.common.exception.HttpStatus4xxException;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * {@link InMemoryDeviceRouteResolver} 的测试。
 */
class InMemoryDeviceRouteResolverTest {

    @Test
    void shouldLoadDirectDeviceRoute() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        DeviceRouteConfig config = new DeviceRouteConfig();
        config.setProductKey("light");
        config.setDeviceCode("light001");
        config.setAccessType("DIRECT_DEVICE");
        properties.setDevices(List.of(config));

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        resolver.init();

        DeviceRoute route = resolver.resolve("light", "light001");
        assertThat(route.getAccessType()).isEqualTo(AccessType.DIRECT_DEVICE);
        assertThat(route.getProductKey()).isEqualTo("light");
        assertThat(route.getDeviceCode()).isEqualTo("light001");
        assertThat(route.getGatewayId()).isNull();
    }

    @Test
    void shouldLoadGatewaySubDeviceRoute() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        DeviceRouteConfig config = new DeviceRouteConfig();
        config.setProductKey("sensor");
        config.setDeviceCode("sensor001");
        config.setAccessType("GATEWAY_SUB_DEVICE");
        config.setGatewayId("gw001");
        properties.setDevices(List.of(config));

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        resolver.init();

        DeviceRoute route = resolver.resolve("sensor", "sensor001");
        assertThat(route.getAccessType()).isEqualTo(AccessType.GATEWAY_SUB_DEVICE);
        assertThat(route.getProductKey()).isEqualTo("sensor");
        assertThat(route.getDeviceCode()).isEqualTo("sensor001");
        assertThat(route.getGatewayId()).isEqualTo("gw001");
    }

    @Test
    void shouldThrowForUnknownDevice() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        properties.setDevices(List.of());

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        resolver.init();

        assertThatThrownBy(() -> resolver.resolve("unknown", "unknown"))
                .isInstanceOf(HttpStatus4xxException.class)
                .hasMessageContaining("目标设备不存在");
    }

    @Test
    void shouldTerminateOnDuplicateDeviceConfig() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        DeviceRouteConfig config1 = new DeviceRouteConfig();
        config1.setProductKey("light");
        config1.setDeviceCode("light001");
        config1.setAccessType("DIRECT_DEVICE");

        DeviceRouteConfig config2 = new DeviceRouteConfig();
        config2.setProductKey("light");
        config2.setDeviceCode("light001");
        config2.setAccessType("DIRECT_DEVICE");

        properties.setDevices(List.of(config1, config2));

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        assertThatThrownBy(resolver::init)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("设备路由配置重复");
    }

    @Test
    void shouldTerminateWhenDirectDeviceHasGatewayId() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        DeviceRouteConfig config = new DeviceRouteConfig();
        config.setProductKey("light");
        config.setDeviceCode("light001");
        config.setAccessType("DIRECT_DEVICE");
        config.setGatewayId("gw001");

        properties.setDevices(List.of(config));

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        assertThatThrownBy(resolver::init)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("直连设备不允许配置 gatewayId");
    }

    @Test
    void shouldTerminateWhenGatewaySubDeviceMissingGatewayId() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        DeviceRouteConfig config = new DeviceRouteConfig();
        config.setProductKey("sensor");
        config.setDeviceCode("sensor001");
        config.setAccessType("GATEWAY_SUB_DEVICE");

        properties.setDevices(List.of(config));

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        assertThatThrownBy(resolver::init)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("网关子设备必须配置 gatewayId");
    }

    @Test
    void shouldTerminateOnInvalidAccessType() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        DeviceRouteConfig config = new DeviceRouteConfig();
        config.setProductKey("light");
        config.setDeviceCode("light001");
        config.setAccessType("INVALID_TYPE");

        properties.setDevices(List.of(config));

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        assertThatThrownBy(resolver::init)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("accessType 不合法");
    }

    @Test
    void shouldTerminateOnMissingProductKey() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        DeviceRouteConfig config = new DeviceRouteConfig();
        config.setDeviceCode("light001");
        config.setAccessType("DIRECT_DEVICE");

        properties.setDevices(List.of(config));

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        assertThatThrownBy(resolver::init)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("缺少 productKey 或 deviceCode");
    }

    @Test
    void shouldHandleEmptyDeviceList() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        properties.setDevices(List.of());

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        resolver.init();

        assertThatThrownBy(() -> resolver.resolve("any", "any"))
                .isInstanceOf(HttpStatus4xxException.class);
    }

    @Test
    void shouldHandleNullDeviceList() {
        DeviceRouteProperties properties = new DeviceRouteProperties();
        properties.setDevices(null);

        InMemoryDeviceRouteResolver resolver = new InMemoryDeviceRouteResolver(properties);
        resolver.init();

        assertThatThrownBy(() -> resolver.resolve("any", "any"))
                .isInstanceOf(HttpStatus4xxException.class);
    }
}
