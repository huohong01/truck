package com.hsdi.NetMe.models.events;

public class EventMeetingUpdate {
    public static final int TYPE_JOINED = 1;
    public static final int TYPE_LEFT = 2;
    public static final int TYPE_REJECTED = 3;

    private final int updateType;
    private final long meetingId;

    public EventMeetingUpdate(int updateType, long meetingId) {
        this.updateType = updateType;
        this.meetingId = meetingId;
    }

    public int getUpdateType() {
        return updateType;
    }

    public long getMeetingId() {
        return meetingId;
    }
}
