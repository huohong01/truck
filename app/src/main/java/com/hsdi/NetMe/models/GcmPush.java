package com.hsdi.NetMe.models;

import android.content.Intent;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hsdi.NetMe.NetMeApp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GcmPush {

    // content keys
    private static final String COMMAND_KEY = "cmd";
    private static final String TOKENS_KEY = "tokens";
    private static final String MESSAGE_KEY = "message";
    private static final String MEETING_ID_KEY = "meetingId";
    private static final String MESSAGE_ID_KEY = "messageId";
    private static final String CHAT_ID_KEY = "chatId";
    private static final String USERNAME_KEY = "user";
    private static final String COLLAPSE_KEY = "collapse_key";
    private static final String MEETING_TYPE_KEY = "meetingType";
    private static final String TYPING_TYPE_KEY = "typingType";


    // command value keys
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            NEW_MEETING_KEY,
            ACCEPTED_MEETING_KEY,
            REJECTED_MEETING_KEY,
            LEAVE_MEETING_KEY,
            NEW_CHAT_KEY,
            NEW_MSG_KEY,
            EDITED_MSG_KEY,
            REMOVED_MSG_KEY,
            SEND_TYPE_KEY
    })
    public @interface Key {
    }

    public static final String NEW_MEETING_KEY = "startMeeting";
    public static final String ACCEPTED_MEETING_KEY = "joinMeeting";
    public static final String REJECTED_MEETING_KEY = "rejectMeetingInvite";
    public static final String LEAVE_MEETING_KEY = "leaveMeeting";
    public static final String NEW_CHAT_KEY = "getChat";
    public static final String NEW_MSG_KEY = "sendChatMessage";
    public static final String EDITED_MSG_KEY = "editChatMessage";
    public static final String REMOVED_MSG_KEY = "removeChatMessage";
    public static final String SEND_TYPE_KEY = "sendTypingType";

    private String command;
    private String token;
    private String meetingId;
    private String chatId;
    private String messageId;
    private String message;
    private String collapseKey;
    private String senderUsername;
    private String senderDisplayName;
    private String meetingType;
    private String typingType;

//--------------------------------------------------------------------------------------------------


    public GcmPush(Intent intent) {
        command = intent.getStringExtra(COMMAND_KEY);
        token = intent.getStringExtra(TOKENS_KEY);
        meetingId = intent.getStringExtra(MEETING_ID_KEY);
        chatId = intent.getStringExtra(CHAT_ID_KEY);
        messageId = intent.getStringExtra(MESSAGE_ID_KEY);
        senderUsername = intent.getStringExtra(USERNAME_KEY);
        message = intent.getStringExtra(MESSAGE_KEY);
        collapseKey = intent.getStringExtra(COLLAPSE_KEY);
        meetingType = intent.getStringExtra(MEETING_TYPE_KEY);
        typingType = intent.getStringExtra(TYPING_TYPE_KEY);

        senderDisplayName = intent.getStringExtra(MESSAGE_KEY)
                .replace(" has invited you to a video chat!", "")
                .trim();

        token = token.replaceAll("^\\[\"", "");
        token = token.replaceAll("\"\\]$", "");
    }


//--------------------------------------------------------------------------------------------------


    @Key
    public String getCommand() {
        return command;
    }

    public boolean hasValidCommand() {
        return command != null && !command.isEmpty() &&
                (
                        command.equals(NEW_MEETING_KEY) ||
                                command.equals(ACCEPTED_MEETING_KEY) ||
                                command.equals(REJECTED_MEETING_KEY) ||
                                command.equals(LEAVE_MEETING_KEY) ||
                                command.equals(NEW_CHAT_KEY) ||
                                command.equals(NEW_MSG_KEY) ||
                                command.equals(EDITED_MSG_KEY) ||
                                command.equals(REMOVED_MSG_KEY) ||
                                command.equals(SEND_TYPE_KEY)
                );
    }

    public String getToken() {
        return token;
    }

    public String getMeetingIdString() {
        return meetingId;
    }

    public long getMeetingId() {
        try {
            return Long.parseLong(meetingId);
        } catch (Exception e) {
            Log.d("GcmPush", "failed to parse meeting id", e);
            return -1;
        }
    }

    public String getChatIdString() {
        return chatId;
    }

    public long getChatId() {
        try {
            return Long.parseLong(chatId);
        } catch (Exception e) {
            Log.d("GcmPush", "failed to parse chat id", e);
            return -1;
        }
    }

    public String getMessageIdString() {
        return messageId;
    }

    public long getMessageId() {
        try {
            return Long.parseLong(messageId);
        } catch (Exception e) {
            Log.d("GcmPush", "failed to parse message id", e);
            return -1;
        }
    }

    public int getMeetingType() {
        try {
            return Integer.parseInt(meetingType);
        } catch (Exception e) {
            Log.d("GcmPush", "failed to parse meeting type", e);
            return -1;
        }
    }
    public int getTypingType() {
        try {
            return Integer.parseInt(typingType);
        } catch (Exception e) {
            Log.d("GcmPush", "failed to parse typingType", e);
            return -1;
        }
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFromCurrentUser() {
        String cu = NetMeApp.getCurrentUser();
        String normUsername = PhoneNumberUtil.normalizeDigitsOnly(senderUsername);
        String normDisplayName = PhoneNumberUtil.normalizeDigitsOnly(senderDisplayName);

        return normUsername.equals(cu) || normDisplayName.equals(cu);
    }


//--------------------------------------------------------------------------------------------------


    @Override
    public String toString() {
        return "---------------------------Received Notification Start-------------------\n" +
                "GcmPush{" +
                "\n\tcommand='" + command + '\'' +
                ", \n\ttokens=" + token +
                ", \n\tmeetingId='" + meetingId + '\'' +
                ", \n\tchatId='" + chatId + '\'' +
                ", \n\tmessageId='" + messageId + '\'' +
                ", \n\tmessage='" + message + '\'' +
                ", \n\tcollapseKey='" + collapseKey + '\'' +
                ", \n\tsenderUsername='" + senderUsername + '\'' +
                ", \n\tsenderDisplayName='" + senderDisplayName + '\'' +
                ", \n\tmeetingType='" + meetingType + '\'' +
                ", \n\ttypingType='" + typingType + '\'' +
                '}' +
                "\n---------------------------Received Notification End---------------------";
    }
}
