package com.alibaba.cloud.ai.examples.chatbot.tool;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.ToolParam;
import java.util.function.BiFunction;

public class UserLocationTool implements BiFunction<String, ToolContext, String> {
    @Override
    public String apply(
            @ToolParam(description = "User query") String query,
            ToolContext toolContext) {
        // 从上下文中获取用户信息
        String userId = (String) toolContext.getContext().get("user_id");
        return "1".equals(userId) ? "Florida" : "San Francisco";
    }
}
