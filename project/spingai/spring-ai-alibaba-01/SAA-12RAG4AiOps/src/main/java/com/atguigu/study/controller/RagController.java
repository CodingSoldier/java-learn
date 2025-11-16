package com.atguigu.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RagController
{

    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;
    @Resource
    private VectorStore vectorStore;

    /**
     * http://localhost:8012/rag1?msg=00000
     * http://localhost:8012/rag1?msg=C2222
     */
    @GetMapping("/rag1")
    public Flux<String> rag1(String msg) {
        String systemInfo = """
                你是一个运维工程师,按照给出的编码给出对应故障解释,否则回复找不到信息。
                """;
        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).build();
        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();
        return chatClient
                .prompt()
                .system(systemInfo)
                .user(msg)
                .advisors(advisor)
                .stream()
                .content();
    }

}
