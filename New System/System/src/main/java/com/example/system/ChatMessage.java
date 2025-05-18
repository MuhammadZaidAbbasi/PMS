package com.example.system;

import java.security.Timestamp;

public class ChatMessage {
    private final String senderId;
    private final String messageText;
    private final java.sql.Timestamp sentTime;

    public ChatMessage(String senderId, String messageText, java.sql.Timestamp sentTime) {
        this.senderId = senderId;
        this.messageText = messageText;
        this.sentTime = sentTime;
    }

    public String getSenderId() { return senderId; }
    public String getMessageText() { return messageText; }
    public java.sql.Timestamp getSentTime() { return sentTime; }
}
