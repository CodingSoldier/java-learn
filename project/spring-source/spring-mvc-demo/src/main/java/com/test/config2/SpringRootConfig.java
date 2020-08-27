package com.test.config2;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SpringContext中相关的bean
 */
@Configuration
@ComponentScan("com.test.service")
public class SpringRootConfig {
}
