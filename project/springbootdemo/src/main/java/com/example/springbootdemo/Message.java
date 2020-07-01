package com.example.springbootdemo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenpiqian
 * @date: 2020-06-30
 */
@Data
@AllArgsConstructor
public class Message {
    public enum MessageType {
        HI,
        GOODBYE,
        CHAT
    }

    private MessageType messageType;
    private String target;
}
