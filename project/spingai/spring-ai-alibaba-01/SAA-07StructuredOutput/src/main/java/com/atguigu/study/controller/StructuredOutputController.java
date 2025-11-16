package com.atguigu.study.controller;

import com.atguigu.study.records.StudentRecord;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@RestController
public class StructuredOutputController
{
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("/structuredOutput1")
    public StudentRecord structuredOutput1(@RequestParam(name = "sname") String sname,
                                           @RequestParam(name = "email") String email) {
        Consumer<ChatClient.PromptUserSpec> consumer = new Consumer<>() {
            @Override
            public void accept(ChatClient.PromptUserSpec promptUserSpec) {
                promptUserSpec.text("学生学号是学号1001，我叫{sname},大学专业计算机科学与技术,邮箱{email}")
                        .param("sname", sname)
                        .param("email", email);
            }
        };
        return qwenChatClient.prompt().user(consumer).call().entity(StudentRecord.class);
    }


    @GetMapping("/structuredOutput2")
    public StudentRecord structuredOutput2(@RequestParam(name = "sname") String sname,
                                           @RequestParam(name = "email") String email) {
        return qwenChatClient.prompt()
                .user(spec -> spec.text("学生学号是学号1111，我叫{sname},大学专业是汉语言文学,邮箱{email}")
                    .param("sname", sname)
                        .param("email", email)
                )
                .call()
                .entity(StudentRecord.class);
    }

}
