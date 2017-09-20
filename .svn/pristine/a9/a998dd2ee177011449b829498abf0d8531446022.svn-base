package com.hsdi.NetMe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.database.DatabaseContract.ChatTracker;
import com.hsdi.NetMe.models.Chat;
import com.hsdi.NetMe.models.Message;
import com.hsdi.NetMe.models.Participant;
import com.hsdi.NetMe.models.Phone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatTrackerManager extends ChatTracker {
    private static final String TAG = "ChatTrackerManager";

    /**
     * Gets the first chat id associated with a contacts list of phone number
     * @param context    context
     * @param phones     a list of phone numbers to check
     * @return the chat id if found, an empty string otherwise
     * */
    public static long getChatIdFromPhoneList(Context context, List<Phone> phones){
        SQLiteDatabase  db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor;

        String selection = COLUMN_NAME_USERNAME + " = ?";

        for(Phone phone : phones) {
            String selectionArgs[] = {phone.getPlainNumber()};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                String chatId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CHAT_ID));
                cursor.close();
                return Long.valueOf(chatId);
            }

            cursor.close();
        }

        return -1;
    }

    /**
     * Gets the chat id for an existing chat which involves a list of participants
     * @param context         context
     * @param participants    the list of participants which must be involved in the chat
     * @return the chat id if found, otherwise {@code null} is returned
     * */
    public static long getChatIdFromParticipants(Context context, List<Participant> participants) {
        List<String> usernameList = new ArrayList<>();
        String currentUser = NetMeApp.getCurrentUser();

        for(Participant participant : participants) {
            String username = participant.getUsername();
            if(!username.equals(currentUser)) usernameList.add(username);
        }

        return getChatIdFromUserList(context, usernameList);
    }

    /**
     *  Gets the chat id for an existing chat which involves a list of users
     *  @param context         context
     *  @param usernameList    a list of the usernames/phone numbers which must be
     *                         involves in the chat
     * @return the chat id if found, otherwise {@code null} is returned
     * */
    public static long getChatIdFromUserList(Context context, List<String> usernameList) {
        Collections.sort(usernameList);
        String userListString = usernameList.toString().replace("[", "").replace("]", "").replace(" ", "");
        return getChatIdFromPhoneNumber(context, userListString);
    }

    /**
     * Gets the chat id associated with a contacts phone number
     * @param context     context
     * @param phoneNumber the contact's phone number
     * @return the chat id if found, -1 otherwise
     * */
    public static long getChatIdFromPhoneNumber(Context context, String phoneNumber){
        SQLiteDatabase  db = DatabaseHelper.getInstance(context).getReadableDatabase();

        String selection = COLUMN_NAME_USERNAME + " = ?";
        String selectionArgs[] = {phoneNumber};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) {
            /*TODO update this to grab an integer instead*/
            String chatId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CHAT_ID));
            cursor.close();
            return Long.valueOf(chatId);
        }

        cursor.close();

        return -1;
    }

    /**
     * Gets the chat name associated with a chat id
     * @param context    context
     * @param chatId     the id for the chat
     * @return the chat name if found, null otherwise
     * */
    public static String getChatName(Context context, long chatId) {
        SQLiteDatabase  db = DatabaseHelper.getInstance(context).getReadableDatabase();

        String selection = COLUMN_NAME_CHAT_ID + " = ?";
        String selectionArgs[] = {String.valueOf(chatId)};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) {
            String chatName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CHAT_NAME));
            cursor.close();
            return chatName;
        }

        cursor.close();

        return null;
    }

    /**
     * Returns all the chats stored on the Chat Tracker Database
     * @param context    the context
     * @return the list of chat if any were found, an empty list otherwise
     * */
    public static List<Chat> getAllChats(Context context) {
        List<Chat> chatList = new ArrayList<>();
        SQLiteDatabase  database = null;
        Cursor cursor = null;

        try {
            database = DatabaseHelper.getInstance(context).getReadableDatabase();

            cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                // get chat info
                long chatId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CHAT_ID));
                String chatName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CHAT_NAME));
                long chatMsgId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LATEST_MESSAGE_ID));
                long chatMsgMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LAST_MESSAGE_MILLIS));
                int chatTotalParticipants = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TOTAL_PARTICIPANTS));
                String chatUsernames = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME));

                // get participants involved
                List<String> usernameList = new ArrayList<>();
                String[] usernameArray = chatUsernames.replace("[","").replace("]","").split(",");
                for (String username : usernameArray) {usernameList.add(username.replaceAll(",",""));}

                List<Participant> participants = ParticipantManager.getParticipantsWithUsernames(context, usernameList);
                Log.i("yuyong_messages", "getAllChats: chatUsernames =" + chatUsernames + ",participants = " + participants.toString());
                // get the latest message
                Message message = ChatMessageManager.getMessage(context, chatMsgId);
                List<Message>  messageList = new ArrayList<>();
                messageList.add(message);

                // create the chat
                chatList.add(new Chat(chatId, messageList, participants, chatMsgMillis, 1, chatTotalParticipants, 0, chatName));
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to get the Chats from the Chat Tracker Database", e);
        }
        finally {
            if(cursor != null) cursor.close();
            // if(database != null) database.close();
        }

        return chatList;
    }


    public static Chat getChatWithChatId(Context context,long chatId){
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        String selection = COLUMN_NAME_CHAT_ID + " = ?";
        String selectionArgs[] = {String.valueOf(chatId)};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Chat chat = null;
        if (cursor.moveToFirst()){

            String chatName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CHAT_NAME));
            long chatMsgId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LATEST_MESSAGE_ID));
            long chatMsgMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_LAST_MESSAGE_MILLIS));
            int chatTotalParticipants = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TOTAL_PARTICIPANTS));
            String chatUsernames = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME));

            // get participants involved
            List<String> usernameList = new ArrayList<>();
            String[] usernameArray = chatUsernames.replace("[","").replace("]","").split(",");
            for (String username : usernameArray) {usernameList.add(username.replaceAll(",",""));}
            List<Participant> participants = ParticipantManager.getParticipantsWithUsernames(context, usernameList);
            Log.i("yuyong_messages", "getChatWithChatId: chatUsernames =" + chatUsernames + ",participants = " + participants.toString());
            // get the latest message
            Message message = ChatMessageManager.getMessage(context, chatMsgId);
            List<Message>  messageList = new ArrayList<>();
            messageList.add(message);
            chat = new Chat(chatId, messageList, participants, chatMsgMillis, 1, chatTotalParticipants, 0, chatName);
        }
        return chat;

    }

//--------------------------------------------------------------------------------------------------


    /**
     * Adds a chat to the Chat Tracker Database
     * @param context    the context
     * @param chat       the chat to be added
     * */
    public static void storeChat(Context context, Chat chat) {
        if(chat == null) return;

        long chatId = chat.getChatId();
        List<Participant> participants = chat.getParticipants();
        Message latestMsg = chat.getLatestMessage();

        long msgId = -1;
        long msgSeconds = -1;
        int totalParticipants = 0;

        if(latestMsg != null) {
            msgId = latestMsg.getId();
            msgSeconds = chat.getLatestMessageMillis();
            totalParticipants = chat.getTotalParticipants();
        }

        storeChatIdWithParticipants(context, participants, chatId, msgId, msgSeconds, totalParticipants);
    }

    /**
     * Adds an association between the chat id and the username/phone numbers involved in the chat
     * @param context    context
     * @param phones     the username/phone numbers involved in the chat
     * @param chatId     the chat id
     * */
    public static void storeChatIdWithPhones(Context context, List<Phone> phones, long chatId, long latestMessageId, long latestMessageMillis, int totalParticipants) {
        List<String> numberList = new ArrayList<>();
        String currentUser = NetMeApp.getCurrentUser();

        for(Phone phone : phones) {
            String number = phone.getNumber();

            if(!number.equals(currentUser)) numberList.add(number);
        }

        storeChatIdWithUserList(context, numberList, chatId, latestMessageId, latestMessageMillis, totalParticipants);
    }

    /**
     * Adds an association between the chat id and the participants involved in the chat
     * @param context         context
     * @param participants    the participants involved in the chat
     * @param chatId          the chat id
     * */
    public static void storeChatIdWithParticipants(Context context, List<Participant> participants, long chatId, long latestMessageId, long latestMessageMillis, int totalParticipants) {
        List<String> usernameList = new ArrayList<>();
        String currentUser = NetMeApp.getCurrentUser();

        if(participants != null) {
            for (Participant participant : participants) {
                String username = participant.getUsername();
                // if (!username.equals(currentUser)) usernameList.add(username);
                usernameList.add(username);
            }
        }

        storeChatIdWithUserList(context, usernameList, chatId, latestMessageId, latestMessageMillis, totalParticipants);
    }

    /**
     * Adds an association between the chat id and the users involved in the chat
     * @param context         context
     * @param usernameList    the users involved in the chat
     * @param chatId          the chat id
     * */
    public static void storeChatIdWithUserList(Context context, List<String> usernameList, long chatId, long latestMessageId, long latestMessageMillis, int totalParticipants) {
        Collections.sort(usernameList);
        String userListString = usernameList.toString().replace("[", "").replace("]", "").replace(" ", "");
        storeChatId(context, userListString, chatId, latestMessageId, latestMessageMillis, totalParticipants);
    }

    /**
     * Stores or updates a chat id associated to a phone number
     * @param context   context
     * @param username  the contact's phone number/username in this chat
     * @param chatId    the chat id to be stored
     * */
    public static void storeChatId(Context context, String username, long chatId, long latestMessageId, long latestMessageMillis, int totalParticipants){
        SQLiteDatabase  db = DatabaseHelper.getInstance(context).getWritableDatabase();

        // username values are currently only phone numbers, so make sure that the plain version is stored
        // username = PhoneNumberUtil.normalizeDigitsOnly(username);

        ContentValues values = getContentValues(username, String.valueOf(chatId), latestMessageId, latestMessageMillis, totalParticipants);

        String selection = COLUMN_NAME_CHAT_ID + " = ?";
        String selectionArgs[] = {String.valueOf(chatId)};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        // if found through lookup key
        if(cursor.moveToFirst()) db.update(TABLE_NAME, values, selection, selectionArgs);
            // if not, check through username
        else {
            selection = COLUMN_NAME_CHAT_ID + " = ?";
            selectionArgs = new String[] {String.valueOf(chatId)};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // if found through username
            if (cursor.moveToFirst()) db.update(TABLE_NAME, values, selection, selectionArgs);
                // if not, insert new
            else db.insert(TABLE_NAME, null, values);
        }

        cursor.close();
    }

    /**
     * Stores or updates a chat's name associated with a chat id
     * @param context     context
     * @param chatId      the chat id
     * @param chatName    the new name of the chat
     * */
    public static void storeChatName(Context context, long chatId, String chatName) {
        SQLiteDatabase  db = DatabaseHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_CHAT_ID, chatId);
        values.put(COLUMN_NAME_CHAT_NAME, chatName);

        String selection = COLUMN_NAME_CHAT_ID + " = ?";
        String selectionArgs[] = {String.valueOf(chatId)};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) db.update(TABLE_NAME, values, selection, selectionArgs);
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Removes all instances of the chatId from the database
     * @param context    context
     * @param chatId     the chat id to be removed
     * */
    public static void removeChatId(Context context, long chatId) {
        SQLiteDatabase  db = DatabaseHelper.getInstance(context).getWritableDatabase();

        String selection = COLUMN_NAME_CHAT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(chatId)};

        try {db.delete(TABLE_NAME, selection, selectionArgs);}
        catch (Exception e){
            Log.d(TAG, "Failed to delete chat " + chatId + " from chat tracker table", e);
            FlurryAgent.onError(TAG, "Failed to delete chat " + chatId + " from chat tracker table", e);
        }
    }

    /**
     * Creates a {@link ContentValues} object containing the username and chat id passed in
     * @param username             the username
     * @param chatId               the chat id
     * @param latestMessageId      the id for the newest message of the chat
     * @param latestMessageMillis  the string date for the latest message
     * @param totalParticipants    the total number of participants currently invloved in the chat
     * */
    private static ContentValues getContentValues(String username, String chatId, long latestMessageId, long latestMessageMillis, int totalParticipants){
        ContentValues values = new ContentValues();

        if(!TextUtils.isEmpty(username)) values.put(COLUMN_NAME_USERNAME, username);
        if(!TextUtils.isEmpty(chatId)) values.put(COLUMN_NAME_CHAT_ID, chatId);
        if(latestMessageId > -1) values.put(COLUMN_NAME_LATEST_MESSAGE_ID, latestMessageId);
        if(latestMessageMillis > 0) values.put(COLUMN_NAME_LAST_MESSAGE_MILLIS, latestMessageMillis);
        if(totalParticipants > 0) values.put(COLUMN_NAME_TOTAL_PARTICIPANTS, totalParticipants);

        return values;
    }
}
