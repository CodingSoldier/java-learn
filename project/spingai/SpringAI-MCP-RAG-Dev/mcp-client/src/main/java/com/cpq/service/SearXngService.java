package com.cpq.service;

import cn.hutool.json.JSONUtil;
import com.cpq.bean.ChatEntity;
import com.cpq.bean.ChatResponseEntity;
import com.cpq.bean.SearXNGResponse;
import com.cpq.bean.SearchResult;
import com.cpq.enums.SSEMsgType;
import com.cpq.sse.SseServer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearXngService {

    @Value("${internet.websearch.searxng.url}")
    private String SEARXNG_URL;

    @Value("${internet.websearch.searxng.counts}")
    private Integer COUNTS;

    @Autowired
    private OkHttpClient okHttpClient;
    @Autowired
    private ChatClient chatClient;

    public List<SearchResult> search(String query) {
        // 构建url
        HttpUrl url = HttpUrl.get(SEARXNG_URL)
                .newBuilder()
                .addQueryParameter("q", query)
                .addQueryParameter("format", "json")
                .build();

        log.info("搜索的url地址为：" + url.url());

        // 构建request
        Request request = new Request.Builder()
                .url(url)
                .build();

        // 发送请求
        try (Response response = okHttpClient.newCall(request).execute()) {

            // 判断请求是否成功还是失败
            if (!response.isSuccessful()) throw new RuntimeException("请求失败: HTTP " + response.code());

            // 获得响应的数据
            if (response.body() != null) {
                String responseBody = response.body().string();

                SearXNGResponse searXNGResponse = JSONUtil.toBean(responseBody, SearXNGResponse.class);

                return dealResults(searXNGResponse.getResults());
            }
            log.error("搜索失败：{}", response.message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Collections.emptyList();
    }

    private List<SearchResult> dealResults(List<SearchResult> results) {

        return results.subList(0, Math.min(COUNTS, results.size()))
                        .parallelStream()
                        .sorted(Comparator.comparingDouble(SearchResult::getScore).reversed())
                        .limit(COUNTS).toList();
    }


    private static final String SESRXNG_PROMPT = """
                                              你是一个互联网搜索大师，请基于以下互联网返回的结果作为上下文，根据你的理解结合用户的提问综合后，生成并且输出专业的回答：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。
                                              """;

    public void doInternetSearch(ChatEntity chatEntity) {

        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();

        List<SearchResult> searchResults = this.search(question);

        String finalPrompt = buildSesrXngPrompt(question, searchResults);

        // 组装提示词
        Prompt prompt = new Prompt(finalPrompt);

        System.out.println(prompt.toString());

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

    private static String buildSesrXngPrompt(String question, List<SearchResult> searchResults) {

        StringBuilder context = new StringBuilder();

        searchResults.forEach(searchResult -> {
            context.append(
                    String.format("<context>\n[来源] %s \n [摘要] %s \n </context>\n",
                            searchResult.getUrl(),
                            searchResult.getContent()));
        });

        return SESRXNG_PROMPT
                .replace("{context}", context)
                .replace("{question}", question);
    }

}
