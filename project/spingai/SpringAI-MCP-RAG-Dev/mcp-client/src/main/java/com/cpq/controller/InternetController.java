package com.cpq.controller;

import com.cpq.bean.ChatEntity;
import com.cpq.service.SearXngService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("internet")
public class InternetController {

    @Resource
    private SearXngService searXngService;

    @GetMapping("/test")
    public Object test(@RequestParam("query") String query){
        return searXngService.search(query);
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        searXngService.doInternetSearch(chatEntity);
    }

}
