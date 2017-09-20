package com.hsdi.NetMe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flurry.android.FlurryAgent;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.database.DatabaseContract.ChatTracker;
import com.hsdi.NetMe.database.DatabaseContract.ParticipantTracker;
import com.hsdi.NetMe.database.DatabaseContract.ChatMessageTracker;
import com.hsdi.NetMe.database.DatabaseContract.RegisteredContacts;

/**
 * Created by Chau Thai on 10/28/15.
 * <p>
 * Does all base Database actions such as creation, modification, and deletion
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

//--------------------------------- Database Variables ---------------------------------------------

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "database_20170816001.db";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";

//----------------------------------- Create Statements --------------------------------------------

    /*starting database version 4 this table is combined into the registered contacts table*/
    private static final String SQL_CREATE_CHAT_TRACKER =
            CREATE_TABLE + ChatTracker.TABLE_NAME + " ("
                    + ChatTracker._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ChatTracker.COLUMN_NAME_LOOK_UP_KEY + " TEXT,"
                    + ChatTracker.COLUMN_NAME_USERNAME + " TEXT,"
                    + ChatTracker.COLUMN_NAME_CHAT_ID + " TEXT NOT NULL," /*TODO change this to an integer*/
                    + ChatTracker.COLUMN_NAME_CHAT_NAME + " TEXT,"
                    + ChatTracker.COLUMN_NAME_LATEST_MESSAGE_ID + " INTEGER,"
                    + ChatTracker.COLUMN_NAME_LAST_MESSAGE_MILLIS + " INTEGER,"
                    + ChatTracker.COLUMN_NAME_TOTAL_PARTICIPANTS + " INTEGER"
                    + " )";

    private static final String SQL_CREATE_REGISTERED_CONTACTS =
            CREATE_TABLE + RegisteredContacts.TABLE_NAME + " ("
                    + RegisteredContacts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + RegisteredContacts.COLUMN_NAME_CONTACT_ID + " INTEGER NOT NULL,"
                    + RegisteredContacts.COLUMN_NAME_LOGGED_IN_USER_NUMBER + " TEXT DEFAULT " + NetMeApp.getCurrentUser() + ", "
                    + RegisteredContacts.COLUMN_NAME_FAVORITE + " INTEGER DEFAULT 0,"
                    + RegisteredContacts.COLUMN_NAME_REGISTRATION_VALID + " INTEGER DEFAULT 1,"
                    + RegisteredContacts.COLUMN_NAME_CALLING_CODE + " TEXT,"
                    + RegisteredContacts.COLUMN_NAME_REGISTERED_NUMBER + " TEXT NOT NULL,"
                    + RegisteredContacts.COLUMN_NAME_REGISTERED_FIRST_NAME + " TEXT,"
                    + RegisteredContacts.COLUMN_NAME_REGISTERED_LAST_NAME + " TEXT,"
                    + RegisteredContacts.COLUMN_NAME_REGISTERED_AVATAR_URL + " TEXT,"
                    + RegisteredContacts.COLUMN_NAME_REGISTERED_EMAIL + " TEXT,"
                    + RegisteredContacts.COLUMN_NAME_REGISTERED_USER_ID + " INTEGER,"
                    + RegisteredContacts.COLUMN_NAME_CHAT_ID + " INTEGER"
                    + " )";

    private static final String SQL_CREATE_CHAT_MESSAGE_TRACKER =
            CREATE_TABLE + ChatMessageTracker.TABLE_NAME + " ("
                    + ChatMessageTracker.COLUMN_NAME_MESSAGE_ID + " INTEGER PRIMARY KEY,"
                    + ChatMessageTracker.COLUMN_NAME_CHAT_ID + " INTEGER NOT NULL,"
                    + ChatMessageTracker.COLUMN_NAME_SENDER + " TEXT,"
                    + ChatMessageTracker.COLUMN_NAME_SUBJECT + " TEXT,"
                    + ChatMessageTracker.COLUMN_NAME_DATE + " TEXT,"
                    + ChatMessageTracker.COLUMN_NAME_MESSAGE + " TEXT,"
                    + ChatMessageTracker.COLUMN_NAME_MEDIA + " TEXT"
                    + ")";

    private static final String SQL_CREATE_PARTICIPANT_TRACKER =
            CREATE_TABLE + ParticipantTracker.TABLE_NAME + " ("
                    + ParticipantTracker.COLUMN_NAME_ID + " TEXT PRIMARY KEY,"
                    + ParticipantTracker.COLUMN_NAME_AVATAR + " TEXT,"
                    + ParticipantTracker.COLUMN_NAME_USERNAME + " TEXT,"
                    + ParticipantTracker.COLUMN_NAME_FIRST_NAME + " TEXT,"
                    + ParticipantTracker.COLUMN_NAME_LAST_NAME + " TEXT"
                    + ")";

    private static final String SQL_CREATE_MEETING_TRACKER =
            CREATE_TABLE + DatabaseContract.MeetingTracker.TABLE_NAME + " ("
                    + DatabaseContract.MeetingTracker.COLUMN_NAME_MEETING_ID + " INTEGER PRIMARY KEY,"
                    + DatabaseContract.MeetingTracker.COLUMN_NAME_CHAT_ID + " INTEGER NOT NULL,"
                    + DatabaseContract.MeetingTracker.COLUMN_NAME_START_DATE + " TEXT,"
                    + DatabaseContract.MeetingTracker.COLUMN_NAME_END_DATA + " TEXT,"
                    + DatabaseContract.MeetingTracker.COLUMN_NAME_STATUS + " TEXT,"
                    + DatabaseContract.MeetingTracker.COLUMN_NAME_TOTAL_PARTICIPANTS + " INTEGER"
                    + ")";

//---------------------------------- Alteration Statements -----------------------------------------

    private static final String SQL_DELETE_CHAT_TRACKER = DROP_TABLE + ChatTracker.TABLE_NAME;

    private static final String SQL_DELETE_REGISTERED_CONTACTS = DROP_TABLE + RegisteredContacts.TABLE_NAME;

    private static final String SQL_DELETE_CHAT_MESSAGE_TRACKER = DROP_TABLE + ChatMessageTracker.TABLE_NAME;

    private static final String SQL_DELETE_PARTICIPANT_TRACKER = DROP_TABLE + ParticipantTracker.TABLE_NAME;

    private static final String SQL_DELETE_MEETING_TRACKER = DROP_TABLE + DatabaseContract.MeetingTracker.TABLE_NAME;

    private static final String SQL_UPGRADE_TO_DB_2 =
            "ALTER TABLE " + ChatTracker.TABLE_NAME + " ADD "
                    + ChatTracker.COLUMN_NAME_CHAT_NAME + " TEXT";

    private static final String SQL_UPGRADE_TO_DB_4_PART_1 =
            "ALTER TABLE " + ChatTracker.TABLE_NAME + " ADD "
                    + ChatTracker.COLUMN_NAME_LATEST_MESSAGE_ID + " INTEGER";

    private static final String SQL_UPGRADE_TO_DB_4_PART_2 =
            "ALTER TABLE " + ChatTracker.TABLE_NAME + " ADD "
                    + ChatTracker.COLUMN_NAME_LAST_MESSAGE_MILLIS + " INTEGER";

    private static final String SQL_UPGRADE_TO_DB_4_PART_3 =
            "ALTER TABLE " + ChatTracker.TABLE_NAME + " ADD "
                    + ChatTracker.COLUMN_NAME_TOTAL_PARTICIPANTS + " INTEGER";


//--------------------------------------------------------------------------------------------------

    private static DatabaseHelper instance;

    /**
     * Always use singleton to avoid weird multi-thread database bug
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            createAll(db);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError(TAG, "Failed to setup the database", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 2:
                db.execSQL(SQL_UPGRADE_TO_DB_2);
            case 3:
            case 4:
                db.execSQL(SQL_UPGRADE_TO_DB_4_PART_1);
                db.execSQL(SQL_UPGRADE_TO_DB_4_PART_2);
                db.execSQL(SQL_UPGRADE_TO_DB_4_PART_3);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteAll(db);
        createAll(db);
    }

    private void createAll(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CHAT_TRACKER);
        db.execSQL(SQL_CREATE_REGISTERED_CONTACTS);
        db.execSQL(SQL_CREATE_CHAT_MESSAGE_TRACKER);
        db.execSQL(SQL_CREATE_PARTICIPANT_TRACKER);
        db.execSQL(SQL_CREATE_MEETING_TRACKER);
    }

    private void deleteAll(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_CHAT_TRACKER);
        db.execSQL(SQL_DELETE_REGISTERED_CONTACTS);
        db.execSQL(SQL_DELETE_CHAT_MESSAGE_TRACKER);
        db.execSQL(SQL_DELETE_PARTICIPANT_TRACKER);
        db.execSQL(SQL_DELETE_MEETING_TRACKER);
    }

    private void resetDb(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            deleteAll(db);
            createAll(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError(TAG, "Failed to reset the database", e);
        } finally {
            db.endTransaction();
        }
    }

    public static void logoutDatabase(Context context) {
        SQLiteDatabase database = getInstance(context).getWritableDatabase();

        database.execSQL(SQL_DELETE_CHAT_TRACKER);
        database.execSQL(SQL_DELETE_PARTICIPANT_TRACKER);
        database.execSQL(SQL_DELETE_MEETING_TRACKER);
        database.execSQL(SQL_DELETE_CHAT_MESSAGE_TRACKER);

        database.execSQL(SQL_CREATE_CHAT_TRACKER);
        database.execSQL(SQL_CREATE_CHAT_MESSAGE_TRACKER);
        database.execSQL(SQL_CREATE_PARTICIPANT_TRACKER);
        database.execSQL(SQL_CREATE_MEETING_TRACKER);
        database.close();
    }
}
