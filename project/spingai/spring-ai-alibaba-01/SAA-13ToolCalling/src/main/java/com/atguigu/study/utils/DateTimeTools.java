package com.atguigu.study.utils;

import org.springframework.ai.tool.annotation.Tool;

public class DateTimeTools {

    /**
     * 1.定义 function call（tool call）
     * 2. returnDirect
     *    true = tool直接返回不走大模型，直接给客户
     *    false = 默认值，拿到tool返回的结果，给大模型，最后由大模型回复
     */
    @Tool(description = "获取当前时间", returnDirect = false)
    public String getCurrentTime() {
        return "现在时间是：" + java.time.LocalDateTime.now();
    }

}
