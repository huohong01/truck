package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.models.Chat;

public class GetChatResponse extends BaseResponse {
    @SerializedName("chatId")
    int chatId;
    @SerializedName("Chat")
    Chat chat;

    public GetChatResponse(Boolean success, String chatId, Chat chat) {
        this.success = success ? 1 : 0;
        this.chat = chat;
        try {this.chatId = Integer.parseInt(chatId);}
        catch (Exception e) { this.chatId = -1; }
    }

    public int getChatId() {
        return chatId;
    }

    public Chat getChat() {
        return chat;
    }
}
