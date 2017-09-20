package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.models.Meeting;

import java.util.List;

public class GetMeetingLogsResponse extends BaseResponse {
    @SerializedName("Meetings")
    private List<Meeting> meetings;

    public List<Meeting> getMeetings() {
        return meetings;
    }
}
