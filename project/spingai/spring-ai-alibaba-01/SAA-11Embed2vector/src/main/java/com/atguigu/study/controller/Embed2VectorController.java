package com.atguigu.study.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Slf4j
public class Embed2VectorController {

    @Resource
    private EmbeddingModel embeddingModel;
    @Resource
    private VectorStore vectorStore;

    @GetMapping("/text2embed")
    public EmbeddingResponse text2embed(String text) {
        EmbeddingRequest embeddingRequest = new EmbeddingRequest(List.of(text), DashScopeEmbeddingOptions.builder().withModel("text-embedding-v3").build());
        EmbeddingResponse response = embeddingModel.call(embeddingRequest);
        log.info("########{}", response.getResult().getOutput());
        return response;
    }

    @GetMapping("/embed2vector/add")
    public void add() {
        List<Document> documents = List.of(
                new Document("与LLM有关"),
                new Document("小说内容"),
                new Document("i love java"),
                new Document("春风若有怜花意")
        );
        vectorStore.add(documents);
    }

    @GetMapping("/embed2vector/query")
    public List<Document> query(String text) {
        SearchRequest request = SearchRequest.builder().query(text).topK(3).build();
        List<Document> documents = vectorStore.similaritySearch(request);
        return documents;
    }


}
