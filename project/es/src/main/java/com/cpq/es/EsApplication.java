package com.cpq.es;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class EsApplication {

    @Autowired
    private TransportClient client;

    //通过id获取数据
    @GetMapping("/get/book/novel")
    public ResponseEntity get1(@RequestParam(name = "id", defaultValue = "") String id) {
        GetResponse result = client.prepareGet("book", "novel", id).get();
        System.out.println(result.toString());
        return new ResponseEntity(result.getSource(), HttpStatus.OK);
    }

    //增加
    @PostMapping("/add/book/novel")
    public ResponseEntity add1(@RequestBody Map<String, String> map) throws Exception {
        XContentBuilder content = XContentFactory.jsonBuilder()
                .startObject()
                .field("title", map.get("title"))
                .field("author", map.get("author"))
                .field("word_count", map.get("wordCount"))
                .field("publish_date", map.get("publishDate"))
                .endObject();
        IndexResponse result = client.prepareIndex("book", "novel")
                .setSource(content)
                .get();
        return new ResponseEntity(result.getResult(), HttpStatus.OK);

    }

    //指定id新增、id存在则为更新
    @PostMapping("/add/book/novel/id")
    public ResponseEntity addid(@RequestBody Map<String, String> map) throws Exception {
        XContentBuilder content = XContentFactory.jsonBuilder()
                .startObject()
                .field("title", map.get("title"))
                .field("author", map.get("author"))
                .field("word_count", map.get("wordCount"))
                .field("publish_date", map.get("publishDate"))
                .endObject();
        IndexResponse result = client.prepareIndex("book", "novel", map.get("id"))
                .setSource(content)
                .get();
        return new ResponseEntity(result.getResult(), HttpStatus.OK);

    }

    //更新
    @PostMapping("/update/book/novel")
    public ResponseEntity update(@RequestBody Map<String, String> map) throws Exception {
        String title = map.get("title");
        String author = map.get("author");
        String wordCount = map.get("wordCount");
        String publishDate = map.get("publishDate");

        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        if (!StringUtils.isEmpty(title)) {
            builder.field("title", title);
        }
        if (!StringUtils.isEmpty(author)) {
            builder.field("author", author);
        }
        if (!StringUtils.isEmpty(wordCount)) {
            builder.field("word_count", wordCount);
        }
        if (!StringUtils.isEmpty(publishDate)) {
            builder.field("publish_date", publishDate);
        }
        builder.endObject();

        UpdateRequest update = new UpdateRequest("book", "novel", map.get("id"));
        update.doc(builder);

        UpdateResponse result = client.update(update).get();

        return new ResponseEntity(result.getResult(), HttpStatus.OK);
    }

    //通过id删除
    @PostMapping("/delete/book/novel")
    public ResponseEntity delete1(@RequestBody Map<String, String> map) throws Exception {

        DeleteResponse result = client.prepareDelete("book", "novel", map.get("id")).get();

        return new ResponseEntity(result.getResult(), HttpStatus.OK);

    }

    //复合查询
    @PostMapping("/query/book/novel")
    public ResponseEntity query(@RequestBody Map<String, String> map) throws Exception {
        String title = map.get("title");
        String author = map.get("author");
        String gtWordCount = map.get("gtWordCount");
        String ltWordCount = map.get("ltWordCount");
        String publishDate = map.get("publishDate");

        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(title)) {
            booleanQuery.must(QueryBuilders.matchQuery("title", title));
        }
        if (!StringUtils.isEmpty(author)) {
            booleanQuery.must(QueryBuilders.matchQuery("author", author));
        }

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("word_count").from(gtWordCount);
        if (!StringUtils.isEmpty(ltWordCount)) {
            rangeQuery.to(ltWordCount);
        }

        booleanQuery.filter(rangeQuery);

        SearchRequestBuilder builder = client.prepareSearch("book").setTypes("novel")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(booleanQuery)
                .setFrom(0).setSize(10);

        System.out.println(builder);

        SearchResponse response = builder.get();
        List<Map<String, Object>> result = new ArrayList();
        for (SearchHit hit : response.getHits()) {
            result.add(hit.getSourceAsMap());
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }


    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }

}

