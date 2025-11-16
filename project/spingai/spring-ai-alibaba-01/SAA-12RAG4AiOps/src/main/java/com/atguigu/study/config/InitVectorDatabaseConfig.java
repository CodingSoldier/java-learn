package com.atguigu.study.config;

import cn.hutool.crypto.SecureUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class InitVectorDatabaseConfig {

    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("classpath:ops.txt")
    private Resource opsTxt;

    @PostConstruct
    public void init() {
        //1 读取文件
        TextReader textReader = new TextReader(opsTxt);
        textReader.setCharset(Charset.defaultCharset());

        //2 文件转换为向量(开启分词)
        List<Document> list = new TokenTextSplitter().transform(textReader.read());
        String fileMd5 = "";
        try {
            fileMd5 = SecureUtil.md5(opsTxt.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String redisKey = "vector-opstxt:" + fileMd5;
        // 判断是否存入过,redisKey如果可以成功插入表示以前没有过，可以假如向量数据
        Boolean retFlag = redisTemplate.opsForValue().setIfAbsent(redisKey, "1");
        if (Boolean.TRUE.equals(retFlag)) {
            // 写入向量数据库RedisStack
            vectorStore.add(list);
        }
    }

}
