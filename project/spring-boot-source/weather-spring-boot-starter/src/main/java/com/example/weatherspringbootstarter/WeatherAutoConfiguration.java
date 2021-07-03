package com.example.weatherspringbootstarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * @EnableConfigurationProperties的作用是使WeatherProperties配置类生效
 * @ConditionalOnProperty 配置了weather.enable=true的时候，本类生效，并将WeatherAutoConfiguration加入IOC容器
 *
 *
 */
@EnableConfigurationProperties(WeatherProperties.class)
@ConditionalOnProperty(name = "weather.enable", havingValue = "true")
public class WeatherAutoConfiguration {
    @Autowired
    private WeatherProperties weatherProperties;

    /**
     * @ConditionalOnMissingBean 如果ioc容器中没有WeatherTemplate，则本bean生效
     */
    @Bean
    @ConditionalOnMissingBean(WeatherTemplate.class)
    public WeatherTemplate weatherService(){
        return new WeatherTemplate(weatherProperties);
    }
}
