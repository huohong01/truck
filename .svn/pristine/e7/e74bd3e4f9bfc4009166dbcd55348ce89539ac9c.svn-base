package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.models.Message;

import java.util.List;

public class SendMessageResponse extends BaseResponse {
    @SerializedName("Message")
    Message message;

    @SerializedName("Messages")
    List<Message> messages;

    public Message getMessage() {
        if(message != null) return message;
        else if (haveMsgs()) return messages.get(0);
        else return null;
    }

    public boolean haveMsgs() {
        return message != null || (messages != null && messages.size() > 0);
    }
}
