package com.atguigu.study.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

@Slf4j
@RestController
public class Text2VoiceController {

    @Resource
    private SpeechSynthesisModel speechsynthesisModel;

    /**
     * http://localhost:8010/t2v/voice
     */
    @GetMapping("/t2v/voice")
    public String voice(@RequestParam(name = "msg", defaultValue = "温馨提示，支付宝到账100万元，请注意查收")String msg) {
        String filePath = "D:\\" + UUID.randomUUID() + ".mp3";
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                .model("cosyvoice-v2")
                .voice("longyingcui")
                .build();
        SpeechSynthesisResponse response = speechsynthesisModel.call(new SpeechSynthesisPrompt(msg, options));
        ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(byteBuffer.array());
        } catch (Exception ex) {
            log.error("", ex);
        }
        return filePath;
    }
}
