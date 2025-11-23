package com.cpq.service;

import cn.hutool.json.JSONUtil;
import com.cpq.bean.ChatEntity;
import com.cpq.bean.ChatResponseEntity;
import com.cpq.enums.SSEMsgType;
import com.cpq.sse.SseServer;
import com.cpq.util.CustomTextSplitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RagService {

    @Autowired
    private RedisVectorStore redisVectorStore;
    @Autowired
    private ChatClient chatClient;

    public List<Document> loadText(Resource resource, String fileName) {

        // 加载读取文档
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("fileName", fileName);
        List<Document> documentList = textReader.get();

//        System.out.println("documentList = " + documentList);

//        默认的文本切分器
//        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//        List<Document> list = tokenTextSplitter.apply(documentList);

        CustomTextSplitter tokenTextSplitter = new CustomTextSplitter();
        List<Document> list = tokenTextSplitter.apply(documentList);

        System.out.println("list = " + list);

        // 向量存储
        int batchSize = 10;
        for (int i = 0; i < list.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, list.size());
            List<Document> batch = list.subList(i, endIndex);
            redisVectorStore.add(batch);
        }

        return documentList;
    }

    public List<Document> vectorSearch(String question) {
        return redisVectorStore.similaritySearch(question);
    }


    // Dify 智能体引擎构建平台

    private static final String RAG_PROMPT = """
                                              基于上下文的知识库内容回答问题：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。不相关的近似内容不必提到。
                                              """;

    public void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext) {

        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();

        // 构建提示词
        String context = null;
        if (ragContext != null && ragContext.size() > 0) {
            context = ragContext.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));
        }

        // 组装提示词
        Prompt prompt = new Prompt(RAG_PROMPT
                .replace("{context}", context)
                .replace("{question}", question));

        System.out.println(prompt);

        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        List<String> list = stringFlux.toStream().map(chatResponse -> {
            String content = chatResponse.toString();
            SseServer.sendMsg(userId, content, SSEMsgType.ADD);
            log.info("content: {}", content);
            return content;
        }).collect(Collectors.toList());

        String fullContent = list.stream().collect(Collectors.joining());

        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);

        SseServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);

    }

}
