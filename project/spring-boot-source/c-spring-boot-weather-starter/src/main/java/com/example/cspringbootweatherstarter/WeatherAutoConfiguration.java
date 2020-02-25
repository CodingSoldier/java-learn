package com.example.cspringbootweatherstarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
WeatherSource.class 只使用了 @ConfigurationProperties(prefix = "weather") 注解，没使用@Component，不会注入到IOC容器。
 @EnableConfigurationProperties(WeatherSource.class) 就是把带有@ConfigurationProperties的WeatherSource注入IOC
 */
@EnableConfigurationProperties(WeatherSource.class)
@ConditionalOnProperty(name = "weather.enable", havingValue = "true")
public class WeatherAutoConfiguration {

    /**
     * @EnableConfigurationProperties(WeatherSource.class)已经将WeatherSource注入了IOC
     */
    @Autowired
    private WeatherSource weatherSource;

    /**
     * 使用weatherSource创建weatherService，并将weatherService注入IOC
     * @ConditionalOnMissingBean(WeatherService.class)IOC中没有WeatherService才创建此bean
     * @return weatherService
     */
    @Bean
    @ConditionalOnMissingBean(WeatherService.class)
    public WeatherService weatherService(){
        return new WeatherService(weatherSource);
    }

}
