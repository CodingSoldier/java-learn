package com.cpq.util;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.List;

public class CustomTextSplitter extends TextSplitter {

    @Override
    protected List<String> splitText(String text) {
        return List.of(split(text));
    }

    public String[] split(String text) {
        return text.split("\\s*\\R\\s*\\R\\s*");
    }
}
