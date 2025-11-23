package com.cpq.controller;

import com.cpq.bean.ChatEntity;
import com.cpq.bean.LeeResult;
import com.cpq.service.RagService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("rag")
public class RagController {

    @Resource
    private RagService ragService;

    @PostMapping("/uploadRagDoc")
    public LeeResult uploadRagDoc(@RequestParam("file") MultipartFile file ){
        List<Document> documentList =  ragService.loadText(file.getResource(), file.getOriginalFilename());
        return LeeResult.ok(documentList);
    }

    @GetMapping("/doSearch")
    public LeeResult doSearch(@RequestParam String question) {
        return LeeResult.ok(ragService.vectorSearch(question));
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response) {
        List<Document> list = ragService.vectorSearch(chatEntity.getMessage());
        response.setCharacterEncoding("UTF-8");
        ragService.doChatRagSearch(chatEntity, list);
    }

}
