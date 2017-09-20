package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.models.Message;
import com.hsdi.NetMe.models.Participant;

import java.util.List;

public class StartChatResponse extends BaseResponse {
    @SerializedName("chatId")
    private int chatId;
    @SerializedName("totalParticipants")
    private int totalParticipants;
    @SerializedName("Message")
    private Message message;
    @SerializedName("Messages")
    private List<Message> messages;
    @SerializedName("Participants")
    private List<Participant> participants;

    public int getChatId() {
        return chatId;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public Message getMessage() {
        if (message != null) return message;
        else if(messages != null && messages.size() > 0) return messages.get(0);
        else return null;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public boolean haveMsgs() {
        return messages != null && messages.size() > 0;
    }

    public boolean haveParticipants() {
        return participants != null && participants.size() > 0;
    }
}
