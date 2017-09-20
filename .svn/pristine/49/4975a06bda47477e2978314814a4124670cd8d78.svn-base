package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.models.Message;
import com.hsdi.NetMe.models.Participant;

import java.util.List;

public class StartMeetingResponse extends BaseResponse{

    @SerializedName("meetingId")
    int meetingId;
    @SerializedName("token")
    String token;
    @SerializedName("sessionId")
    String sessionId;
    @SerializedName("started")
    String started;
    @SerializedName("ended")
    String ended;
    @SerializedName("status")
    String status;
    @SerializedName("owner")
    int ownerId;
    @SerializedName("chatId")
    int chatId;
    @SerializedName("totalParticipants")
    int totalParticipants;
    @SerializedName("startDate")
    String startDate;
    @SerializedName("endDate")
    String endDate;
    @SerializedName("Participants")
    List<Participant> participants;
    @SerializedName("Messages")
    List<Message> messages;
    @SerializedName("Message")
    Message message;

    public int getMeetingId() {
        return meetingId;
    }

    public String getToken() {
        return token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getStarted() {
        return started;
    }

    public String getEnded() {
        return ended;
    }

    public String getStatus() {
        return status;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getChatId() {
        return chatId;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Message getMessage() {
        if(message != null) return message;
        else if(messages != null && messages.size() > 0) return messages.get(0);
        else return null;
    }
}