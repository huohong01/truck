package com.hsdi.NetMe.database;

import android.provider.BaseColumns;

/**
 * Holds all the table reference information for the Favorite and ChatTracker tables
 */
final class DatabaseContract {

    /**
     * The table name and columns for the chat tracker database
     */
    static abstract class ChatTracker implements BaseColumns {
        static final String TABLE_NAME = "ChatTracker";
        static final String COLUMN_NAME_LOOK_UP_KEY = "lookupKey";
        static final String COLUMN_NAME_USERNAME = "username";
        static final String COLUMN_NAME_CHAT_ID = "chatId";
        static final String COLUMN_NAME_CHAT_NAME = "chatName";
        static final String COLUMN_NAME_LATEST_MESSAGE_ID = "latestMsgId";
        static final String COLUMN_NAME_LAST_MESSAGE_MILLIS = "lastMsgMillis";
        static final String COLUMN_NAME_TOTAL_PARTICIPANTS = "totalParticipants";
    }

    /**
     * The table name and columns for the participant tracker database
     */
    static abstract class ParticipantTracker implements BaseColumns {
        static final String TABLE_NAME = "Participants";
        static final String COLUMN_NAME_ID = "userId";
        static final String COLUMN_NAME_AVATAR = "avatarUrl";
        static final String COLUMN_NAME_FIRST_NAME = "firstName";
        static final String COLUMN_NAME_LAST_NAME = "lastName";
        static final String COLUMN_NAME_USERNAME = "username";
    }

    /**
     * The table name and columns for the registered contact database
     */
    static abstract class RegisteredContacts implements BaseColumns {
        static final String TABLE_NAME = "RegisteredContacts";
        static final String COLUMN_NAME_CONTACT_ID = "contactId";
        static final String COLUMN_NAME_LOGGED_IN_USER_NUMBER = "logged_in_user";
        static final String COLUMN_NAME_FAVORITE = "favorite";
        static final String COLUMN_NAME_REGISTRATION_VALID = "valid_reg";
        static final String COLUMN_NAME_CALLING_CODE = "calling_code";
        static final String COLUMN_NAME_REGISTERED_NUMBER = "phone";
        static final String COLUMN_NAME_REGISTERED_FIRST_NAME = "first_name";
        static final String COLUMN_NAME_REGISTERED_LAST_NAME = "last_name";
        static final String COLUMN_NAME_REGISTERED_AVATAR_URL = "avatar";
        static final String COLUMN_NAME_REGISTERED_EMAIL = "email";
        static final String COLUMN_NAME_REGISTERED_USER_ID = "user_id";
        static final String COLUMN_NAME_CHAT_ID = "chatId";
    }

    /**
     * The table name and columns for the chat message tracker database
     */
    static abstract class ChatMessageTracker {
        static final String TABLE_NAME = "ChatMessageManager";
        static final String COLUMN_NAME_MESSAGE_ID = "MessageId";
        static final String COLUMN_NAME_CHAT_ID = "ChatId";
        static final String COLUMN_NAME_SENDER = "Sender";
        static final String COLUMN_NAME_SUBJECT = "Subject";
        static final String COLUMN_NAME_MESSAGE = "Message";
        static final String COLUMN_NAME_DATE = "Date";
        static final String COLUMN_NAME_MEDIA = "Media";
    }

    /**
     * The table name and columns for the chat meeting tracker database
     */
    static abstract class MeetingTracker {
        static final String TABLE_NAME = "Meetings";
        static final String COLUMN_NAME_MEETING_ID = "MeetingId";
        static final String COLUMN_NAME_CHAT_ID = "ChatId";
        static final String COLUMN_NAME_START_DATE = "StartDate";
        static final String COLUMN_NAME_END_DATA = "EndDate";
        static final String COLUMN_NAME_STATUS = "Status";
        static final String COLUMN_NAME_TOTAL_PARTICIPANTS = "TotalParticipants";
    }
}
