package com.designpattern.bridgemode.message;

public interface UrgencyMessage extends Message{
    Object watch(String messageId);
}
