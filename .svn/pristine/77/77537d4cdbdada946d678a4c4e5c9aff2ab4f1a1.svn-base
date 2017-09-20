package com.hsdi.NetMe.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.models.response_models.BaseResponse;
import com.hsdi.NetMe.util.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Service class used to send logout messages (which removes a gcm token from being updated),
 * endMeeting messages (CloseVideo, ends the current video),
 * leaveMeeting messages (LeaveVideo, removes this user from the list of participants for a meeting),
 * and rejectMeeting messages (RejectVideo, rejects an incoming video meeting invite).
 * This service runs in the background whether the app is running or not, until it is done, then it closes.
 * */
public class EndMeetingService extends Service {
    private static final String TAG = "Network";

    public static final String EXTRA_MEETING_ID = "meetingId";
    public static final String EXTRA_SERVICE_CMD = "serviceCommand";

    public static final int CMD_CLOSE_VIDEO = 1;
    public static final int CMD_LEAVE_VIDEO = 2;
    public static final int CMD_REJECT_VIDEO = 3;

    private static int runningCount = 0;
    private final Callback<BaseResponse> defaultCallback = new Callback<BaseResponse>() {
        @Override
        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
            shutDown();
        }

        @Override
        public void onFailure(Call<BaseResponse> call, Throwable t) {
            shutDown();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        runningCount = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runningCount += 1;
        if(intent == null){
            Log.e(TAG, "network service, intent is null");
            shutDown();
            return super.onStartCommand(null, flags, startId);
        }

        // get the meeting id
        long meetingId = intent.getLongExtra(EXTRA_MEETING_ID, -1);

        if(meetingId > -1) {
            // get the requested command and run it
            switch (intent.getIntExtra(EXTRA_SERVICE_CMD, -1)) {
                case CMD_CLOSE_VIDEO:
                    closeVideo(meetingId);
                    break;

                case CMD_LEAVE_VIDEO:
                    leaveVideo(meetingId);
                    break;

                case CMD_REJECT_VIDEO:
                    rejectVideo(meetingId);
                    break;

                default:
                    shutDown();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void shutDown(){
        runningCount -= 1;
        if(runningCount <= 0) stopSelf();
    }

    /**
     * Sends a message to alert all relevant parties that this user is closing this video meeting.
     * */
    private void closeVideo(long meetingId) {
        PreferenceManager manager = NetMeApp.getInstance().getPrefManager();

        RestServiceGen.getUnCachedService()
                .closeVideo(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        meetingId
                )
                .enqueue(defaultCallback);
    }

    /**
     * Sends a message to alert all relevant parties that this user is leaving this video meeting.
     * */
    private void leaveVideo(long meetingId) {
        PreferenceManager manager = NetMeApp.getInstance().getPrefManager();

        RestServiceGen.getUnCachedService()
                .leaveVideo(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        meetingId
                )
                .enqueue(defaultCallback);
    }

    /**
     * Sends a message to alert all relevant parties that this user has rejected this video meeting
     * invite.
     * */
    private void rejectVideo(long meetingId) {
        PreferenceManager manager = NetMeApp.getInstance().getPrefManager();

        RestServiceGen.getUnCachedService()
                .rejectVideo(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        meetingId
                )
                .enqueue(defaultCallback);
    }
}
