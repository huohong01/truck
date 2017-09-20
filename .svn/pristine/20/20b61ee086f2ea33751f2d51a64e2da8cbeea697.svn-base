package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.models.Chat;

import java.util.List;

public class GetChatLogsResponse extends BaseResponse{
    @SerializedName("Chats")
    List<Chat> chats;
    @SerializedName("totalChats")
    int totalChats;
    @SerializedName("lastChatDate")
    String lastChatDate;

    public List<Chat> getChats() {
        return chats;
    }

    public int getTotalChats() {
        return totalChats;
    }

    public String getLastChatDate() {
        return lastChatDate;
    }
}
