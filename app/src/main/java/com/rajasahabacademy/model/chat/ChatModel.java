package com.rajasahabacademy.model.chat;

public class ChatModel {
    String message;
    String chatType = "";

    public ChatModel(String message, String chatType) {
        this.message = message;
        this.chatType = chatType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
