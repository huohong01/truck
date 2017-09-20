package com.hsdi.NetMe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.hsdi.NetMe.models.Meeting;
import com.hsdi.NetMe.models.Participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huohong.yi on 2017/7/28.
 */

public class MeetingManager extends DatabaseContract.MeetingTracker {

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

    public static List<Meeting> getAllMeetings(Context context){
        List<Meeting> meetingList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase(context);
        Cursor cursor = null;
        try{
            cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                long meetingId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_MEETING_ID));
                long chatId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CHAT_ID));
                String startDateString = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_START_DATE));
                String endDateString = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_END_DATA));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STATUS));
                String meetingUsernames = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TOTAL_PARTICIPANTS));

                //得到Participants
                List<String> usernameList= new ArrayList<>();
                String[] usernames = meetingUsernames.replace("[","").replace("]","").split(",");
                for (String username:usernames) {
                    usernameList.add(username.replaceAll(",",""));
                    Log.i("yuyong_messages", "getAllMeetings: username =" + username );
                }

                List<Participant> participants = ParticipantManager.getParticipantsWithUsernames(context,usernameList);
                Log.i("yuyong_messages", "getAllMeetings: meetingUsernames =" + meetingUsernames + ",participants = " + participants.toString());
                meetingList.add(new Meeting(meetingId,chatId,startDateString,endDateString,status,participants));
            }
        }catch (Exception e){
            Log.i("yuyong_message", "Failed to get the Meeting Database." + e.getMessage());
        }
        finally {
            if (cursor !=null) cursor.close();
        }
        return meetingList;
    }

    public static void storeMeeting(Context context, Meeting meeting){
        if (meeting == null) return;
        long meetingId = meeting.getMeetingId();
        long chatId = meeting.getChatId();
        String startDateString = meeting.getStartDateString();
        String endDateString = meeting.getEndDateString();
        String status = meeting.getStatus();
        List<Participant> participants = meeting.getParticipants();
        storeMeetingWithParticipants(context,meetingId,chatId,startDateString,endDateString,status,participants);
    }

    public static void storeMeetingWithParticipants(Context context,long meetingId,long chatId,String startDateString,String endDateString,String status,List<Participant> participants){
        List<String> usernameList = new ArrayList<>();
        if (participants != null){
            for (Participant participant:participants){
                String username = participant.getUsername();
                usernameList.add(username);
            }
        }
        Collections.sort(usernameList);
        //去掉字符串中的[ ,] ,空格之类的，保留的格式：13242079379,13510202803
        String usernameListString = usernameList.toString().replace("[","").replace("]","").replace(" ","");

        storeMeetingWithMeetingId(context,meetingId,chatId,startDateString,endDateString,status,usernameListString);
    }

    public static void storeMeetingWithMeetingId(Context context,long meetingId,long chatId,String startDateString,String endDateString,String status,String usernameListString){
        SQLiteDatabase db = getWritableDatabase(context);
        ContentValues values = getContentValues(meetingId,chatId,startDateString,endDateString,status,usernameListString);

        String selection = COLUMN_NAME_MEETING_ID + " = ?";
        String selectionArgs[] = {String.valueOf(meetingId)};

        Cursor cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);
        //先
        if (cursor.moveToFirst()) db.update(TABLE_NAME,values,selection,selectionArgs);

        else {
            selection = COLUMN_NAME_MEETING_ID + " = ?";
            selectionArgs = new String[]{String.valueOf(meetingId)};
            cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);

            //如果通过meetingId找到，就直接更新
            if (cursor.moveToFirst()) db.update(TABLE_NAME,values,selection,selectionArgs);
            //如果没有找到，就重新插入
            else db.insert(TABLE_NAME,null,values);
        }
        cursor.close();

    }

    private static ContentValues getContentValues( long meetingId, long chatId, String startDateString,String endDateString,String status,String usernameListString){
        ContentValues values = new ContentValues();

        if(meetingId > -1) values.put(COLUMN_NAME_MEETING_ID, meetingId);
        if(chatId > -1) values.put(COLUMN_NAME_CHAT_ID, chatId);
        if(!TextUtils.isEmpty(startDateString)) values.put(COLUMN_NAME_START_DATE, startDateString);
        if(!TextUtils.isEmpty(endDateString)) values.put(COLUMN_NAME_END_DATA, endDateString);
        if(!TextUtils.isEmpty(status)) values.put(COLUMN_NAME_STATUS, status);
        if(!TextUtils.isEmpty(usernameListString)) values.put(COLUMN_NAME_TOTAL_PARTICIPANTS, usernameListString);

        return values;
    }

}
