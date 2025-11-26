package com.cpq.config;

import com.cpq.mcp.tool.DateTool;
import com.cpq.mcp.tool.EmailTool;
import com.cpq.mcp.tool.ProductTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    /**
     * 将工具方法暴露给外部 mcp client 调用
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(DateTool dateTool, EmailTool emailTool,
                                                     ProductTool productTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTool, emailTool, productTool)
                .build();
    }

}
