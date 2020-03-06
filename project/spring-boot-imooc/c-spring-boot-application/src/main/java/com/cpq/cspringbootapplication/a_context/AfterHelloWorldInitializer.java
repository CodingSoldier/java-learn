package com.cpq.cspringbootapplication.a_context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

// 排序方式2。实现Ordered接口
public class AfterHelloWorldInitializer<C extends ConfigurableApplicationContext> implements ApplicationContextInitializer<C>, Ordered {

    @Override
    public void initialize(C applicationContext) {
        System.out.println("启动 AfterHelloWorldInitializer");
    }

    // 最低优先级
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
