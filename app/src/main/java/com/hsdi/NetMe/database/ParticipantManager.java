package com.hsdi.NetMe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hsdi.NetMe.database.DatabaseContract.ParticipantTracker;
import com.hsdi.NetMe.models.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantManager extends ParticipantTracker {
    private static final String TAG = "ParticipantManager";

    /**
     * Adds or updates an existing participant on the database
     * @param context        the context
     * @param participant    the participant to add
     * */
    public static void addParticipant(Context context, Participant participant) {
        SQLiteDatabase database = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = null;

        String selection = COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {participant.getUsername()};

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, participant.getUserId());
        values.put(COLUMN_NAME_AVATAR, participant.getAvatarUrl());
        values.put(COLUMN_NAME_USERNAME, participant.getUsername());
        values.put(COLUMN_NAME_FIRST_NAME, participant.getFirstName());
        values.put(COLUMN_NAME_LAST_NAME, participant.getLastName());

        try {
            cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) database.update(TABLE_NAME, values, selection, selectionArgs);
            else database.insert(TABLE_NAME, null, values);
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to add participant", e);
        }
        finally {
            if (cursor != null) cursor.close();
            //if (database != null) database.close();
        }
    }

    /**
     * Adds or updates an existing list of participants on the database
     * */
    public static void addParticipants(Context context, List<Participant> participants) {
        SQLiteDatabase database = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = null;

        for (Participant participant: participants) {
            String selection = COLUMN_NAME_USERNAME + " = ?";
            String[] selectionArgs = {participant.getUsername()};

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_ID, participant.getUserId());
            values.put(COLUMN_NAME_AVATAR, participant.getAvatarUrl());
            values.put(COLUMN_NAME_USERNAME, participant.getUsername());
            values.put(COLUMN_NAME_FIRST_NAME, participant.getFirstName());
            values.put(COLUMN_NAME_LAST_NAME, participant.getLastName());

            try {
                cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

                if(cursor.moveToFirst()) database.update(TABLE_NAME, values, selection, selectionArgs);
                else database.insert(TABLE_NAME, null, values);
            } catch (Exception e) {
                Log.d(TAG, "Failed to add participant", e);
            } finally {
                if (cursor != null) cursor.close();
               // if (database != null) database.close();
            }
        }

    }


//--------------------------------------------------------------------------------------------------


    /**
     * Returns a participant with a matching id
     * @param context    the context
     * @param id         the id of the participant to look for
     * @return a participant if found, null otherwise
     * */
    public static Participant getParticipantWithId(Context context, String id) {
        SQLiteDatabase database = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        Participant participant = getParticipantWithId(database, cursor, id);

        if (cursor != null) cursor.close();
        //if (database != null) database.close();

        return participant;
    }

    /**
     * Returns a list of participants with ids matching the passed list of ids
     * @param context    the context
     * @param ids        a list of participant ids to look for
     * @return all the participants found, if non-found an empty list is returned
     * */
    public static List<Participant> getParticipantsWithIds(Context context, List<String> ids) {
        SQLiteDatabase database = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        List<Participant> participants = new ArrayList<>();

        for (String id : ids) {
            Participant participant = getParticipantWithId(database, cursor, id);

            if(participant != null) participants.add(participant);
        }

        if (cursor != null) cursor.close();
        //if (database != null) database.close();

        return participants;
    }

    /**
     * Returns a participant with a matching username
     * @param context     the context
     * @param username    the username of the participant to look for
     * @return a participant if found, null otherwise
     * */
    public static Participant getParticipantWithUsername(Context context, String username) {
        SQLiteDatabase database = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        Participant participant = getParticipantWithUsername(database, cursor, username);

        if (cursor != null) cursor.close();
        //if (database != null) database.close();

        return participant;
    }

    /**
     * Returns a list of participants with usernames matching the passed list of usernames
     * @param context      the context
     * @param usernames    a list of participant usernames to look for
     * @return all the participants found, if non-found an empty list is returned
     * */
    public static List<Participant> getParticipantsWithUsernames(Context context, List<String> usernames) {
        SQLiteDatabase database = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        List<Participant> participants = new ArrayList<>();

        for (String username : usernames) {
            Participant participant = getParticipantWithUsername(database, cursor, username);

            if(participant != null) participants.add(participant);
        }

        if (cursor != null) cursor.close();
        //if (database != null) database.close();

        return participants;
    }

    /**
     * Internal method to get participants with matching ids
     * @param database    a readable instance of the database
     * @param cursor      a reusable cursor, done to memory optimization
     * @param id          the id to look for
     * @return the participant if found, null otherwise
     * */
    private static Participant getParticipantWithId(SQLiteDatabase database, Cursor cursor, String id) {
        try {
            String selection = COLUMN_NAME_ID + " = ?";
            String[] selectionArgs = {id};

            cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()) {
                String avatarUrl = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR));
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME));
                String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME));

                return new Participant(false, String.valueOf(id), null, avatarUrl, firstName, lastName, username);
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to get the participant", e);
        }

        return null;
    }

    /**
     * Internal method to get participants with a matching username
     * @param database          a readable instance of the database
     * @param cursor            a reusable cursor, done to memory optimization
     * @param username          the username to look for
     * @return the participant if found, null otherwise
     * */
    private static Participant getParticipantWithUsername(SQLiteDatabase database, Cursor cursor, String username) {
        try {
            String selection = COLUMN_NAME_USERNAME + " = ?";
            String[] selectionArgs = {username};

            cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                String userId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String avatarUrl = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR));
                String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME));

                return new Participant(false, userId, null, avatarUrl, firstName, lastName, username);
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to get the participant", e);
        }

        return null;
    }
}
