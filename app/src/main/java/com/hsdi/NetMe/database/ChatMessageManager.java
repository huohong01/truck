package com.hsdi.NetMe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.hsdi.NetMe.database.DatabaseContract.ChatMessageTracker;
import com.hsdi.NetMe.models.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatMessageManager extends ChatMessageTracker {
    private static final String TAG = "ChatMessageManager";

    /**
     * Gets a read only instance of the database
     * @param context    context
     * @return the instance of the database
     * */
    private static SQLiteDatabase getReadableDatabase(Context context) {
        return DatabaseHelper.getInstance(context).getReadableDatabase();
    }

    /**
     * Gets a read and writable instance of the database
     * @param context    context
     * @return the instance of the database
     * */
    private static SQLiteDatabase getWritableDatabase(Context context) {
        return DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * Adds or updates a message to the database
     * @param context      context
     * @param message      the message
     * */
    public static void addMessage(@NonNull Context context, @NonNull Message message) {
        addMessage(
                context,
                message.getId(),
                message.getChatId(),
                message.getSenderUsername(),
                message.getSubject(),
                message.getDate(),
                message.getMessage(),
                Arrays.toString(message.getMedia())
        );
    }

    /**
     * Adds or updates a message to the database using all the message parts
     * @param context      context
     * @param messageId    the id of the message
     * @param chatId       the id of the chat the message belongs to
     * @param sender       the username of the message's sender
     * @param subject      the subject for the message
     * @param date         the date the message was sent
     * @param message      the message contents
     * @param media        the media array in string form
     * */
    public static void addMessage(@NonNull Context context, long messageId, long chatId, @NonNull String sender, @Nullable String subject, @NonNull String date, @Nullable String message, @Nullable String media) {
        SQLiteDatabase db = getWritableDatabase(context);
        Cursor cursor = null;

        // setup the searching variables
        String selection = ChatMessageTracker.COLUMN_NAME_MESSAGE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(messageId)};

        try {
            // perform search
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            addMessage(db, cursor, selection, selectionArgs, messageId, chatId, sender, subject, date, message, media);
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to add message", e);
            FlurryAgent.onError(TAG, "Failed to add message", e);
        }
        finally {
            // close the cursor
            if (cursor != null) cursor.close();
            // close the database
           // if (db != null) db.close();
        }
    }

    /**
     * Adds or updates a list of messages to the database
     * @param context      context
     * @param messages     a list of messages
     * */
    public static void addMessages(@NonNull Context context, @NonNull List<Message> messages) {
        SQLiteDatabase db = getWritableDatabase(context);
        Cursor cursor = null;

        try {
            // for every message
            for (Message message : messages) {
                // setup the searching variables
                String selection = ChatMessageTracker.COLUMN_NAME_MESSAGE_ID + " = ?";
                String[] selectionArgs = {String.valueOf(message.getId())};

                // perform search
                cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

                // add message
                addMessage(
                        db,
                        cursor,
                        selection,
                        selectionArgs,
                        message.getId(),
                        message.getChatId(),
                        message.getSenderUsername(),
                        message.getSubject(),
                        message.getDate(),
                        message.getMessage(),
                        Arrays.toString(message.getMedia())
                );
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to add messages", e);
            FlurryAgent.onError(TAG, "Failed to add messages", e);
        }
        finally {
            // close the cursor
            if (cursor != null) cursor.close();
            // close the database
           // if (db != null) db.close();
        }
    }

    /**
     * Central method to run the add/update code
     * @param database         a writable instance of the database  which should be closed by the caller
     * @param cursor           a reusable cursor which should be closed by the caller
     * @param selection        the query search selection
     * @param selectionArgs    the query search selection arguments
     * @param messageId        the id of the message
     * @param chatId           the id of the chat the message belongs to
     * @param sender           the username of the message's sender
     * @param subject          the subject for the message
     * @param date             the date the message was sent
     * @param message          the message contents
     * @param media            the media array in string form
     * */
    private static void addMessage (SQLiteDatabase database, Cursor cursor, String selection, String[] selectionArgs, long messageId, long chatId, @NonNull String sender, @Nullable String subject, @NonNull String date, @Nullable String message, @Nullable String media) {
        // cleaning the media string
        if (!TextUtils.isEmpty(media)) media = media.replace("[","").replace("]","");

        // set the contents of the new message
        ContentValues values = new ContentValues();
        values.put(ChatMessageTracker.COLUMN_NAME_MESSAGE_ID, messageId);
        values.put(ChatMessageTracker.COLUMN_NAME_CHAT_ID, chatId);
        values.put(ChatMessageTracker.COLUMN_NAME_SENDER, sender);
        values.put(ChatMessageTracker.COLUMN_NAME_SUBJECT, subject);
        values.put(ChatMessageTracker.COLUMN_NAME_DATE, date);
        values.put(ChatMessageTracker.COLUMN_NAME_MESSAGE, message);
        values.put(ChatMessageTracker.COLUMN_NAME_MEDIA, media);

        // if it exists, update it
        if(cursor.moveToFirst()) database.update(TABLE_NAME, values, selection, selectionArgs);
        // if not exists, add
        else database.insert(TABLE_NAME, null, values);
    }

    /**
     * Remove a message with matching id
     * @param context    context
     * @param messageId  the id for the message
     * */
    public static Message deleteMessage(@NonNull Context context, long messageId) {
        if (messageId < 0) return null;

        Message msg = null;

        SQLiteDatabase db = getWritableDatabase(context);
        Cursor cursor = null;

        // setup the searching variables
        String selection = ChatMessageTracker.COLUMN_NAME_MESSAGE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(messageId)};

        try {
            // perform search
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // if the item was found, get the message and remove it from the database
            if (cursor.moveToFirst()) {
                // get message
                msg = getMessage(cursor);

                // remove the message from the database
                db.delete(TABLE_NAME, selection, selectionArgs);
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to delete Message with id " + messageId, e);
            FlurryAgent.onError(TAG, "Failed to delete Message with id " + messageId, e);
        }
        finally {
            // close the cursor
            if (cursor != null) cursor.close();
            // close the database
            //if (db != null) db.close();
        }

        return msg;
    }

    /**
     * Returns the stored message with a matching id
     * @param context    context
     * @param messageId  the id for the message
     * @return a {@link Message} object that was stored or null if nothing was found
     * */
    public static Message getMessage(@NonNull Context context, long messageId) {
        if (messageId < 0) return null;

        SQLiteDatabase db = getReadableDatabase(context);
        Cursor cursor = null;

        // setup the searching variables
        String selection = ChatMessageTracker.COLUMN_NAME_MESSAGE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(messageId)};

        try {
            // perform search
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // if some there is data, get the message
            if (cursor.moveToFirst()) return getMessage(cursor);
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to get Message with id " + messageId, e);
            FlurryAgent.onError(TAG, "Failed to get Message with id " + messageId, e);
        }
        finally {
            // close the cursor
            if (cursor != null) cursor.close();
            // close the database
            //if (db != null) db.close();
        }


        return null;
    }

    /**
     * Returns all the stored messages for a chat
     * @param context    context
     * @param chatId     the id for the chat
     * @return a list of {@link Message} objects that were stored or null if nothing was found
     * */
    public static List<Message> getChatMessages(@NonNull Context context, long chatId) {
        if (chatId < 0) return null;

        SQLiteDatabase db = getReadableDatabase(context);
        Cursor cursor = null;

        // setup the searching variables
        String selection = ChatMessageTracker.COLUMN_NAME_CHAT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(chatId)};

        try {
            // perform search
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // if the cursor has some data
            if (cursor.moveToFirst()) {
                List<Message> messageList = new ArrayList<>();

                // while there is data to get
                do {
                    // get the message item and add to the list
                    messageList.add(getMessage(cursor));
                } while (cursor.moveToNext());

                // return the list
                return messageList;
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to get Messages for chat " + chatId, e);
            FlurryAgent.onError(TAG, "Failed to get Messages for chat " + chatId, e);
        }
        finally {
            // close the cursor
            if (cursor != null) cursor.close();
            // close the database
           // if (db != null) db.close();
        }

        return null;
    }

    /**
     * Central method to retrieve the Message a cursor is pointing to
     * @param cursor    a cursor pointing to a message
     * @return the Message
     * */
    private static Message getMessage(Cursor cursor) {
        // get message values
        long messageId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_MESSAGE_ID));
        long chatId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CHAT_ID));
        String sender = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SENDER));
        String subject = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SUBJECT));
        String date = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));
        String messageText = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MESSAGE));
        String media = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MEDIA));

        // cleaning the media string
        String cleanMedia = media.replace("[","").replace("]","");

        // split media items if not empty
        String[] mediaArray = null;
        if (!TextUtils.isEmpty(cleanMedia)) mediaArray = cleanMedia.split(",");

        // make into a message item and return
        return new Message(messageId, chatId, sender, subject, messageText, date, mediaArray);
    }
}
