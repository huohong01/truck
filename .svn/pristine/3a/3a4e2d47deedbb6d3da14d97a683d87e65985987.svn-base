package com.hsdi.NetMe.models.events;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EventMessageUpdate {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({unknown, newMsg, editedMsg, removedMsg})
    @interface Type {}

    // event types
    public static final String unknown      = "unknown";
    public static final String newMsg       = "sendChatMessage";
    public static final String editedMsg    = "editChatMessage";
    public static final String removedMsg   = "removeChatMessage";

    private String eventType = unknown;
    private long chatId = -1;
    private long messageId = -1;
    private String senderUsername;

    public EventMessageUpdate(@Type String eventType, long chatId, long messageId, String senderUsername) {
        this.eventType = eventType;
        this.chatId = chatId;
        this.messageId = messageId;
        this.senderUsername = senderUsername;
    }

    @Type
    public static String parseCommand(String command) {
        switch (command) {
            case newMsg:
                return newMsg;

            case editedMsg:
                return editedMsg;

            case removedMsg:
                return removedMsg;

            default:
                return unknown;
        }
    }

    @Type
    public String getType() {
        return eventType;
    }

    public long getChatId() {
        return chatId;
    }

    public long getMessageId() {
        return messageId;
    }

    @SuppressWarnings("unused")
    public String getSenderUsername() {
        return senderUsername;
    }

    @Override
    public String toString() {
        return "EventMessageUpdate{" +
                "\neventType='" + eventType + '\'' +
                ",\nchatId=" + chatId +
                ",\nmessageId=" + messageId +
                ",\nsenderUsername='" + senderUsername + '\'' +
                '}';
    }
}