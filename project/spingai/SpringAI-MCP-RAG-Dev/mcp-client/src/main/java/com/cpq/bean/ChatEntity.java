package com.cpq.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity {

    private String currentUserName;
    private String message;
    private String botMsgId;

}
