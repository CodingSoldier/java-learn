package org.cpq.springbootweb01.completable;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RequestMapping("/c1")
@RestController
public class CompletableController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final ConcurrentHashMap<Long, CompletableFuture<Map<String, Object>>> responseMap = new ConcurrentHashMap<>();

    private static final AtomicLong idGenerator = new AtomicLong(123); // 模拟ID生成

    @PostMapping("/send")
    public Map<String, Object> send(@RequestBody Map<String, Object> body) throws Exception {
        System.out.println("发送请求: ");
        Map<String, Object> response = sendAndWayResp(body);
        System.out.println("收到响应: " + response);
        // 继续后续逻辑
        System.out.println("执行后续代码...");
        return response;
    }

    @PostMapping("/resp")
    public String resp(@RequestBody Map<String, Object> body) throws Exception {
        complete(body);
        return "";
    }

    private static Map<String, Object> sendAndWayResp(Map<String, Object> param) {
        long messageId = idGenerator.getAndIncrement();
        // 将请求存入Map并启动发送线程
        responseMap.put(messageId, new CompletableFuture<>());

        param.put("messageId", messageId);
        System.out.println("发送请求，param: " + param);

        CompletableFuture<Map<String, Object>> future = responseMap.get(messageId);
        try {
            return future.get(30, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new HashMap<>();
    }

    private static Map<String, Object> complete(Map<String, Object> body) {
        String messageIdStr = body.get("messageId").toString();
        final Long messageId = Long.parseLong(messageIdStr);
        responseMap.computeIfPresent(messageId, (id, future) -> {
            Object data = body.get("data");
            try {
                String content = objectMapper.writeValueAsString(data);
                Map map = objectMapper.readValue(content, Map.class);
                future.complete(map);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; // 移除已处理的条目
        });
        return new HashMap<>();
    }

}
