package com.atguigu.study.controller;

import com.alibaba.cloud.ai.advisor.DocumentRetrievalAdvisor;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class BailianRagController
{

    @Resource
    private ChatClient chatClient;
    @Resource
    private DashScopeApi dashScopeApi;

    /**
     * http://localhost:8017/bailian/rag/chat?msg=A0001
     */
    @GetMapping("/bailian/rag/chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "00000错误信息")String msg) {
        // indexName在阿里云百炼平台 -》知识库的名称
        // https://bailian.console.aliyun.com/?tab=app#/knowledge-base/detail/6aovdv07rm
        DashScopeDocumentRetrieverOptions options = DashScopeDocumentRetrieverOptions.builder().withIndexName("智能运维").build();
        DashScopeDocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi, options);
        return chatClient.prompt()
                .user(msg)
                .advisors(new DocumentRetrievalAdvisor(retriever))
                .stream()
                .content();
    }


}
