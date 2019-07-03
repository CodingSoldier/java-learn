package com.cpq.app001.dashboard;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description
 * @Author chenpiqian
 * @Date: 2019-07-03
 */
@Configuration
public class HystrixServletDefinitions {

/**
 https://windmt.com/2018/04/16/spring-cloud-5-hystrix-dashboard/
 配置本工程能被dashboard监控到，配置management.endpoints.web.exposure.include无效
 需要添加这个bean
 url：http://localhost:10011/hystrix.stream
 */

    @Bean(name = "hystrixRegistrationBean")
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new HystrixMetricsStreamServlet(), "/hystrix.stream");
        registration.setName("hystrixServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

}
