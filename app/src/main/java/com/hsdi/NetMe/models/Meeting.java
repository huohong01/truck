package com.hsdi.NetMe.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.util.DateUtil;

import java.util.Date;
import java.util.List;

public class Meeting implements Comparable<Meeting> {
    @SerializedName("meeting_id")
    private long meetingId;
    @SerializedName("started")
    private String startDateString;
    @SerializedName("ended")
    private String endDateString;
    @SerializedName("status")
    private String status;
    @SerializedName("chat_id")
    private long chatId;
    @SerializedName("MeetingParticipants")
    private List<Participant> participants;


    //--------------------------------------------------------------------------------------------------
    public Meeting(long meetingId, long chatId, String startDateString, String endDateString, String status, List<Participant> participants) {
        this.meetingId = meetingId;
        this.startDateString = startDateString;
        this.endDateString = endDateString;
        this.status = status;
        this.chatId = chatId;
        this.participants = participants;
    }

    public long getMeetingId() {
        return meetingId;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public String getStatus() {
        return status;
    }

    public long getChatId() {
        return chatId;
    }

    public List<Participant> getParticipants() {
        return participants;
    }


//--------------------------------------------------------------------------------------------------


    public Date getStartDate() {
        return DateUtil.getDateFromStringUTC(startDateString);
    }

    public String getFormattedStartDate() {
        return DateUtil.getStringFromDate(getStartDate(), "EEE, MMM dd");
    }

    public String getFormattedStartTime() {
        return DateUtil.getStringFromDate(getStartDate(), "hh:mm aa");
    }

    public Date getEndDate() {
        return DateUtil.getDateFromStringUTC(endDateString);
    }

    public String getFormattedEndDate() {
        return DateUtil.getElapsedTimeFromNow(getEndDate());
    }

    public long getEndDateInSeconds() {
        return getEndDate().getTime() / 1000;
    }


//--------------------------------------------------------------------------------------------------


    public boolean isGroupMeeting() {
        return participants.size() > 2;
    }

    public boolean isMissedStatus() {
        String status = getCurrentUserParticipant().getStatus();

        return (Participant.STATUS_INVITED).equals(status) ||
                (Participant.STATUS_UNINVITED).equals(status) ||
                (Participant.STATUS_DECLINED).equals(status);
    }

    public boolean isSentStatus() {
        Participant participant = getCurrentUserParticipant();
        return participant != null && participant.isCreator();
    }

    public boolean isReceivedStatus() {
        return !isMissedStatus() && !isSentStatus();
    }

    public boolean hasNoOtherUsers() {
        return participants.size() <= 1;
    }

    public Participant getOtherParticipant() {
        for (Participant participant : participants) {
            if (!participant.getUsername().equals(NetMeApp.getCurrentUser())) return participant;
        }

        return null;
    }

    public Participant getCurrentUserParticipant() {
        for (Participant participant : participants) {
            if (participant.getUsername().equals(NetMeApp.getCurrentUser())) return participant;
        }

        return null;
    }


//--------------------------------------------------------------------------------------------------


    @Override
    public int compareTo(@NonNull Meeting meeting) {
        return Long.compare(meeting.getMeetingId(), getMeetingId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meeting meeting = (Meeting) o;

        return meetingId == meeting.meetingId;

    }
}
