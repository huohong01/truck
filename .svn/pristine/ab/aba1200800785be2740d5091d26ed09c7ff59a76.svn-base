package com.hsdi.NetMe.ui.chat.video;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ChatTrackerManager;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.events.EventMeetingLog;
import com.hsdi.NetMe.models.events.EventMeetingUpdate;
import com.hsdi.NetMe.models.response_models.JoinMeetingResponse;
import com.hsdi.NetMe.models.response_models.StartMeetingResponse;
import com.hsdi.NetMe.network.EndMeetingService;
import com.hsdi.NetMe.network.RestServiceGen;
import com.hsdi.NetMe.ui.chat.ChatActivity;
import com.hsdi.NetMe.ui.chat.ChatInterface;
import com.hsdi.NetMe.util.Constants;
import com.hsdi.NetMe.util.MyLog;
import com.hsdi.NetMe.util.PreferenceManager;
import com.hsdi.NetMe.util.Utils;
import com.opentok.android.AudioDeviceManager;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * controls all aspects of the video. uses the opentoks service to stream and receive the video
 */
public class VideoFragment extends Fragment implements
        VideoInterface,
        Session.SessionListener, Session.ConnectionListener,
        Publisher.PublisherListener, Subscriber.VideoListener {

    private static final String TAG = "VideoFrag";

    private static final int REQUEST_PERMISSIONS = 4444;

    private ChatInterface chatInterface = null;
    private Fragment fragment = null;
    private static String invited = "";

    // time counter variables
    private Timer elapsedTimer = null;
    private long startTime = -1;
    private int seconds = -1;
    private int minutes = -1;

    // OpenTok variables
    private static final String API_KEY = "45196982";
    private static long meetingId = -1;
    private static String token = "";
    private static String sessionId = "";
    private Session mSession = null;
    private Publisher mPublisher = null;
    private Subscriber mSubscriber = null;
    private Handler mHandler = new Handler();

    private AudioManager mAudioManager;
    private SensorManager mSensorManager;
    private Sensor mProximiny;
    private SensorEventListener mEventListener;
    private float f_proximiny; // 当前传感器距离

    // View variables
    @Bind(R.id.video_fragment_holder)
    FrameLayout fragmentHolder;
    @Bind(R.id.video_user_view)
    RelativeLayout publisherView;
    @Bind(R.id.video_main_view)
    RelativeLayout subscriberView;
    @Bind(R.id.video_main_view_off_icon)
    View mainOffIcon;
    @Bind(R.id.video_user_view_off_icon)
    View userOffIcon;
    @Bind(R.id.video_progressbar_container)
    View progressBar;
    @Bind(R.id.video_controls_wrapper)
    View controlViews;
    @Bind(R.id.video_camera)
    ImageView cameraBtn;
    @Bind(R.id.video_mute)
    ImageView muteBtn;
    @Bind(R.id.video_time)
    TextView timeView;


    public VideoFragment() {
        Log.d(TAG, "in video frag constructor");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("yuyong_video", "VideoFragment:onCreateView: ");
        // Inflate the layout for this fragment and setup video views
        View main = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, main);
        EventBus.getDefault().register(this);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        Log.i("yuyong_video_call", "onCreateView：AudioDeviceManager--setOutputMode " + mAudioManager.isWiredHeadsetOn());
        if (mAudioManager.isWiredHeadsetOn()){
            mAudioManager.setSpeakerphoneOn(false);//关闭扬声器
            // mAudioManager.setMode(AudioManager.MODE_RINGTONE);
        }
        intiSensor();
        if (checkPermissions()) initialize();
        else requestVideoPermissions();
        return main;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            chatInterface = (ChatInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ChatInterface");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            chatInterface = (ChatInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChatInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        chatInterface = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        Log.i("yuyong_video", "VideoFragment:onPause: ");
        // pauses the video session if there is one
        if (mSession != null) mSession.onPause();

        // stops the video timer from updating while the UI thread doesn't belong to this activity
        if (elapsedTimer != null) {
            elapsedTimer.cancel();
            elapsedTimer = null;
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("yuyong_video", "VideoFragment:onResume: ");
        // resume the video session if there was one
        if (mSession != null) mSession.onResume();

        // if the timer was already started, continue it. Ignore otherwise
        if (startTime > -1) startTimeCounter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("yuyong_video", "VideoFragment:onRequestPermissionsResult: ");
        if (checkPermissions()) initialize();
        else if (chatInterface != null) chatInterface.finishVideo(false);
        else getActivity().finish();
    }

    /**
     * Sets up the video call screens depending on the type of call
     */
    private void initialize() {
        Log.i("yuyong_video", "initialize: ");
        Bundle extras = getArguments();
        switch (extras.getInt(Constants.EXTRA_TYPE, -1)) {
            // The user is calling someone
            case Constants.TYPE_START:
                Log.i("yuyong_video", "initialize: TYPE_START");
                invited = extras.getString(Constants.EXTRA_INVITED);
                long chatId = extras.getLong(Constants.EXTRA_CHAT_ID, -1);
                showCallingScreen();
                startVideo(chatId);
                break;

            // Someone is calling the user, get the info and alert the user
            case Constants.TYPE_RING:
                Log.i("yuyong_video", "initialize: TYPE_RING");
                meetingId = extras.getLong(Constants.EXTRA_MEETING_ID);
                token = extras.getString(Constants.EXTRA_TOKEN);
                sessionId = extras.getString(Constants.EXTRA_SESSION_ID);

                String number = extras.getString(Constants.EXTRA_INVITED);
                String name = extras.getString(Constants.EXTRA_INVITED_NAME);

                NetMeApp.setCurrentMeeting(meetingId);
                showAnswerScreen(number, name);
                break;

            // Switching calls, just send the join command and connect
            case Constants.TYPE_SWITCH:
                Log.i("yuyong_video", "initialize: TYPE_SWITCH");
                meetingId = extras.getLong(Constants.EXTRA_MEETING_ID);
                token = extras.getString(Constants.EXTRA_TOKEN);
                sessionId = extras.getString(Constants.EXTRA_SESSION_ID);

                NetMeApp.setCurrentMeeting(meetingId);
                answerCall();
                break;

            // Bad info passed, just close EVERYTHING!!
            default:
                /*Should never be here*/
                getActivity().finish();
        }
    }

    private void intiSensor() {

        mProximiny = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);//TYPE_PROXIMITY是距离传感器类型，当然你还可以换成其他的，比如光线传感器
        mEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                f_proximiny = event.values[0];
                mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                if (f_proximiny >= mProximiny.getMaximumRange()) {
                    //setModeNormal();//稍后会展示这个方法
                    //mAudioManager.setMode(AudioManager.MODE_NORMAL);
                    mAudioManager.setSpeakerphoneOn(true);
                    Log.i("yuyong_sensor", "onSensorChanged: MODE_NORMAL");
                } else {
                    // setInCallBySdk();//稍后会展示这个方法
                    mAudioManager.setSpeakerphoneOn(false);//关闭扬声器
                    mAudioManager.setRouting(AudioManager.MODE_IN_COMMUNICATION, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
                    if (getActivity()!= null) {
                        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    }
                    //mAudioManager.setMode(AudioManager.MODE_IN_CALL);
                    Log.i("yuyong_sensor", "onSensorChanged: MODE_IN_CALL");
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        mSensorManager.registerListener(mEventListener, mProximiny, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Creates a timer which will update the elapsed time of the session at the top of the screen
     */
    private void startTimeCounter() {
        Log.i("yuyong_video", "startTimeCounter: ");
        // only set the start time once
        if (startTime < 0) startTime = System.currentTimeMillis();

        elapsedTimer = new Timer();
        elapsedTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        //calculate how much time has passed since the video chat started
                        long millis = Calendar.getInstance().getTimeInMillis() - startTime;
                        seconds = (int) (millis / 1000);
                        minutes = seconds / 60;
                        seconds = seconds % 60;

                        final String formattedTime = String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    timeView.setText(formattedTime);
                                }
                            });
                        }
                    }
                },
                500,
                1000
        );
    }

    /**
     * Gets the id for the meeting which has been started
     */
    public long getMeetingId() {
        Log.i("yuyong_video", "getMeetingId: ");
        return meetingId;
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Returns if permissions to access/use the camera and mic have been granted.
     *
     * @return {@code TRUE} is both have been granted, {@code FALSE} otherwise
     */
    private boolean checkPermissions() {
        Log.i("yuyong_video", "checkPermissions: ");
        int cameraPStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int micPStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

        return cameraPStatus == PackageManager.PERMISSION_GRANTED &&
                micPStatus == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests that the user grant permission to access/use the camera and microphone.
     * Will only request permission which have not been granted.
     */
    private void requestVideoPermissions() {
        Log.i("yuyong_video", "requestVideoPermissions: ");
        FragmentCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                REQUEST_PERMISSIONS
        );
    }


//--------------------------------------------------------------------------------------------------


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    /** EventBus listener for when the invited user decided to accept/join the meeting */
    public void onMeetingUpdate(EventMeetingUpdate eventMeetingUpdate) {
        Log.i("yuyong_video", "onMeetingUpdate: ");
        // ignore if the id does not match the current meeting
        if (eventMeetingUpdate.getMeetingId() != meetingId) return;

        // determining what action to take
        switch (eventMeetingUpdate.getUpdateType()) {
            // takes care of letting the user know that the contact being called has accepted
            //  the call and is joining
            case EventMeetingUpdate.TYPE_JOINED:
                showConnectingScreen();
                break;

            // ends the meeting since the other user either rejected the call or left the meeting
            case EventMeetingUpdate.TYPE_REJECTED:
            case EventMeetingUpdate.TYPE_LEFT:
                chatInterface.finishVideo(false);
                break;
        }
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Changes the fragment in front of all others to be the answering fragment.
     *
     * @param number the number to show on the answer screen
     * @param name   the name to show on the answer screen
     */
    public void showAnswerScreen(String number, String name) {
        Log.i("yuyong_video", "showAnswerScreen: ");
        Bundle answerExtras = new Bundle();
        answerExtras.putString(AnswerFragment.EXTRA_NUMBER, number);
        answerExtras.putString(AnswerFragment.EXTRA_NAME, name);
        fragment = new AnswerFragment();
        fragment.setArguments(answerExtras);
        getChildFragmentManager().beginTransaction()
                .add(R.id.video_fragment_holder, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * Changes the fragment in front of all others to be the calling fragment
     */
    public void showCallingScreen() {
        Log.i("yuyong_video", "showCallingScreen: ");
        Bundle callingExtras = new Bundle();
        callingExtras.putString(CallFragment.EXTRA_NUMBER, invited);
        fragment = new CallFragment();
        fragment.setArguments(callingExtras);
        getChildFragmentManager().beginTransaction()
                .add(R.id.video_fragment_holder, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * Changes the fragment in front of all others to be the connecting fragment
     */
    public void showConnectingScreen() {
        Log.i("yuyong_video", "showConnectingScreen: ");
        Bundle connectingExtras = new Bundle();
        connectingExtras.putString(ConnectingFragment.EXTRA_NUMBER, invited);
        fragment = new ConnectingFragment();
        fragment.setArguments(connectingExtras);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.video_fragment_holder, fragment)
                .commitAllowingStateLoss();

        ////////////////////////////////
       /* if(1==0){
            final Handler handler=new Handler();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showMainVideoScreen();
                        }
                    });
                }
            },2000);
        }*/
    }

    /**
     * Removes all other fragments in front to only show the main video content and controls
     */
    public void showMainVideoScreen() {
        Log.i("yuyong_video", "showMainVideoScreen: ");
        getChildFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

        startTimeCounter();
        fragmentHolder.setVisibility(View.GONE);
    }

    /**
     * Hides the video controls to simulate a switch to full screen.
     */
    public void hideScreenControls() {
        Log.i("yuyong_video", "hideScreenControls: ");
        controlViews.setVisibility(View.GONE);
    }

    /**
     * Shows the video controls
     */
    public void showScreenControls() {
        Log.i("yuyong_video", "showScreenControls: ");
        publisherView.setVisibility(View.VISIBLE);
        controlViews.setVisibility(View.VISIBLE);
    }

    /**
     * Toggles the screen controls and user's camera view visible and non visible states
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.video_main_view)
    protected void toggleScreenControls(View view) {
        Log.i("yuyong_video", "toggleScreenControls: ");
        if (chatInterface != null) {
            // get the current state
            boolean currentMode = controlViews.getVisibility() == View.VISIBLE;

            // toggle the publisher/current user view
            if (currentMode) hideScreenControls();
            else showScreenControls();
        }
    }

    /**
     * Toggles the microphone's current streaming between it's on/off states
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.video_mute)
    protected void toggleMicrophone(View view) {
        Log.i("yuyong_video", "toggleMicrophone: ");
        boolean result = true;

        // toggles whether the publisher streams out any audio
        if (mPublisher != null) {
            result = !mPublisher.getPublishAudio();
            mPublisher.setPublishAudio(result);
        }

        // changes the image of the mute button to represent the state
        if (result) muteBtn.setImageResource(R.drawable.voice_button);
        else muteBtn.setImageResource(R.drawable.voice_select_button);
    }

    /**
     * Gets the ChatActivity to show the text chat and video chat.
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.video_show_chat)
    protected void showTextChat(View view) {
        Log.i("yuyong_video", "showTextChat: ");
        chatInterface.toggleFullScreen(false, Constants.MEETING_VIDEO);
    }

    /**
     * Toggles the camera's current streaming between it's on/off states
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.video_camera)
    protected void toggleCamera(View view) {
        Log.i("yuyong_video", "toggleCamera: ");
        boolean result = true;

        // toggles whether the camera's feed is being streamed out
        if (mPublisher != null) {
            result = !mPublisher.getPublishVideo();
            mPublisher.setPublishVideo(result);
        }

        // update the user icon visibility
        if (result) userOffIcon.setVisibility(View.INVISIBLE);
        else userOffIcon.setVisibility(View.VISIBLE);

        // update the button's image
        if (result) cameraBtn.setImageResource(R.drawable.view_button);
        else cameraBtn.setImageResource(R.drawable.view_select_button);
    }

    /**
     * Gets the ChatActivity to verify that the user wants to end the video chat and then end it.
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.video_end)
    protected void endVideoChat(View view) {
        Log.i("yuyong_video", "endVideoChat: ");
        chatInterface.finishVideo(true);
    }

    /**
     * Attempts to toggle between different cameras (front -> rear, rear -> front)
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.video_switch_camera)
    protected void flipCamera(View view) {
        Log.i("yuyong_video", "flipCamera: ");
        if (mPublisher != null) {
            try {
                mPublisher.swapCamera();
            } catch (Exception e) {
                Log.e(TAG, "Failed to switch camera", e);
            }
        }
    }


/*------------------------------------------------------------------------------------------------*/


    /**
     * Requests the session information to join the video with the matching meetingId class variable.
     */
    @Override
    public void answerCall() {
        Log.i("yuyong_video", "answerCall: ");
        MyLog.writeLog("OkHttp", "answerCall===================================");
        PreferenceManager manager = PreferenceManager.getInstance(getActivity());

        showConnectingScreen();

        RestServiceGen.getUnCachedService()
                .joinMeeting(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        meetingId
                )
                .enqueue(new Callback<JoinMeetingResponse>() {
                    @Override
                    public void onResponse(Call<JoinMeetingResponse> call, Response<JoinMeetingResponse> response) {
                        // if the response is good, connect
                        if (response != null && response.body() != null && response.body().isSuccess()) {
                            JoinMeetingResponse jmr = response.body();
                            // get contents from the response
                            sessionId = jmr.getSessionId();
                            token = jmr.getToken();
                            MyLog.writeLog("OkHttp", "answerCall:onResponse===================================sessionId = " + sessionId + ",token = " + token);
                            // connect the session
                            if (getActivity() != null) sessionConnect();
                        }
                        // if the response is bad end everything
                        else {
                            MyLog.writeLog("OkHttp", "answerCall:onResponse===================================");
                            if (mSession != null) mSession.disconnect();
                            if (chatInterface != null) chatInterface.finishVideo(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<JoinMeetingResponse> call, Throwable t) {
                        t.printStackTrace();
                        MyLog.writeLog("OkHttp", "answerCall:onFailure===================================");
                        // if it failed, end everything
                        if (chatInterface != null) chatInterface.finishVideo(false);
                    }
                });
    }

    /**
     * Requests the session be rejected.
     */
    @Override
    public void denyCall() {
        Log.i("yuyong_video", "denyCall: ");
        MyLog.writeLog("OkHttp", "denyCall===================================");
        Intent intent = new Intent(getActivity(), EndMeetingService.class);
        intent.putExtra(EndMeetingService.EXTRA_SERVICE_CMD, EndMeetingService.CMD_REJECT_VIDEO);
        intent.putExtra(EndMeetingService.EXTRA_MEETING_ID, meetingId);
        getActivity().startService(intent);

        chatInterface.finishVideo(false);
    }

    /**
     * Attempts to cancel the outgoing call.
     */
    @Override
    public void cancelCall() {
        Log.i("yuyong_video", "cancelCall: ");
        MyLog.writeLog("OkHttp", "cancelCall===================================");
        chatInterface.finishVideo(true);
    }

    @Override
    public void sendNotification() {
        chatInterface.finishVideo(true);

        Intent intent = ((ChatActivity) getActivity()).AAA;
        intent.hashCode();
        Log.e("yi", "sendNotification: ============================ intent.hashCode() = " + intent.hashCode());
        String number = intent.getStringExtra(Constants.EXTRA_INVITED);
        Log.e("yi", "sendNotification: ================================ number = " + number);

        startSendNotification(getActivity().getIntent());
    }

    private void startSendNotification(Intent incomingIntent) {
        // try to get the user info
        final String number = incomingIntent.getStringExtra(Constants.EXTRA_INVITED);
        final Contact contact = NetMeApp.getContactWith(number);
        final long meetingId = incomingIntent.getLongExtra(Constants.EXTRA_MEETING_ID, -1);
        Log.e("yi", "startSendNotification: =======================number = " + number);
        // figure out and set the contact name
        final String fromName;
        if (contact != null && contact.getName() != null && !contact.getName().isEmpty()) {
            fromName = contact.getName();
        } else fromName = number;

        Bitmap squarePicture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_notifications);
        Bitmap largeIcon = Utils.getCircleShape(squarePicture, squarePicture.getHeight());

        // building the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity());
        notificationBuilder.setLargeIcon(largeIcon);
        notificationBuilder.setContentTitle("Miss video call");
        // notificationBuilder.setContentTitle(context.getString(R.string.new_message));
        notificationBuilder.setContentText(fromName);
        // notificationBuilder.setContentIntent(chatPendingIntent);
        notificationBuilder.setAutoCancel(true);
        /*notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationBuilder.setVibrate(new long[]{500});*/

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.notify((int) meetingId, notificationBuilder.build());
    }

    /**
     * Runs the api command to start a new video meeting with the invited user.
     * Attempts to find and continue the most recent text chat, otherwise waits for the back end
     * to send it a new one.
     */
    private void startVideo(long chatId) {
        Log.i("yuyong_video", "VideoFragment:startVideo: ");

        PreferenceManager manager = PreferenceManager.getInstance(getActivity());

        if (chatId < 0)
            chatId = ChatTrackerManager.getChatIdFromPhoneNumber(getActivity(), invited);
        MyLog.writeLog("OkHttp", "startVideo===================================" + chatId + ",invited = " + invited);
        RestServiceGen.getUnCachedService()
                .startMeeting(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        invited,
                        chatId > -1 ? String.valueOf(chatId) : "",
                        Constants.MEETING_VIDEO
                )
                .enqueue(new Callback<StartMeetingResponse>() {
                    @Override
                    public void onResponse(Call<StartMeetingResponse> call, Response<StartMeetingResponse> response) {

                        MyLog.writeLog("OkHttp", "startVideo:onResponse===================================" + response.message() + "*****");
                        Log.i("video", "startVideo:onResponse===================================" + response.message() + "*****");
                        if (response != null && response.body() != null) {
                            StartMeetingResponse smr = response.body();
                            // get the params for the meeting
                            meetingId = smr.getMeetingId();
                            sessionId = smr.getSessionId();
                            token = smr.getToken();
                            MyLog.writeLog("OkHttp", "startVideo:onResponse=================================== meetingId = " + meetingId + ",token = " + token);
                            Log.i("video", "startVideo:onResponse=================================== meetingId = " + meetingId + ",token = " + token);
                            // making sure to start the timer which will cancel this if the
                            //      other person doesn't answer
                            // this makes sure the user doesn't just stay on this screen for ever
                            //      waiting for the other person to connect
                            if (fragment instanceof CallFragment)
                                ((CallFragment) fragment).startCancelTimer();

                            // connect to the openTok session using the passed params and setup chat
                            if (getActivity() != null) {
                                sessionConnect();
                                long chatId = smr.getChatId();
                                chatInterface.startChat(chatId);
                                MyLog.writeLog("OkHttp", "startVideo:onResponse=================================== chatId = " + chatId);
                                Log.e("video", "startVideo:onResponse=================================== chatId = " + chatId);
                            }
                        } else onFailure(null, null);
                    }

                    @Override
                    public void onFailure(Call<StartMeetingResponse> call, Throwable t) {
                        MyLog.writeLog("OkHttp", "startVideo:onFailure===================================");
                        if (chatInterface != null) chatInterface.finishVideo(false);
                    }
                });
    }


/*------------------------------------ OpenTok methods Start -------------------------------------*/

    /**
     * Requests that the session be connected using the VideoFragment global token and session id.
     */
    private void sessionConnect() {
        Log.i("yuyong_video", "sessionConnect: ");
        if (mSession == null) {
            MyLog.writeLog("OkHttp", "sessionConnect=================================== ");
            mSession = new Session(getActivity(), API_KEY, sessionId);
            mSession.setSessionListener(this);
            mSession.setConnectionListener(this);
            mSession.connect(token);
            MyLog.writeLog("OkHttp", "sessionConnect=================================== API_KEY = " + API_KEY + ",sessionId = " + sessionId);
            Log.i("yuyong_video_call", String.format("sessionConnect -- %s -- %s", sessionId, token));
        }
        // alert the history fragment to update
        EventBus.getDefault().post(new EventMeetingLog());
    }

    /**
     * Tells the OpenTok Session to disconnect and end
     */
    public void sessionDisconnect() {
        Log.i("yuyong_video", "sessionDisconnect: ");
        MyLog.writeLog("OkHttp", "sessionDisconnect=================================== ");
        if (mSession != null) mSession.disconnect();
    }

    /**
     * Waits 5 seconds before re-attaching the subscriber to it's view to make sure it is ready
     * and has enough data to show the image.
     */
    public void reloadInterface() {
        Log.i("yuyong_video", "reloadInterface: ");
        MyLog.writeLog("OkHttp", "reloadInterface=================================== ");
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mSubscriber != null) {
                            attachSubscriberView(mSubscriber);
                            mainOffIcon.setVisibility(View.INVISIBLE);
                            MyLog.writeLog("OkHttp", "reloadInterface=================================== mainOffIcon.setVisibility(View.INVISIBLE);");
                        }
                    }
                },
                500);
    }

    /**
     * Called after this user has been connected to the OpenTok Session.
     * A Publisher stream is then created and attached to it's corresponding view.
     *
     * @param session the session which was connected
     */
    @Override
    public void onConnected(Session session) {
        Log.i("yuyong_video", "onConnected: ");
        MyLog.writeLog("OkHttp", "onConnected=================================== ");
        if (mPublisher == null) {
            mPublisher = new Publisher(getActivity(), NetMeApp.getCurrentUser());
            mPublisher.setPublisherListener(this);
            attachPublisherView();
            Log.i("yuyong_video_call", "AudioDeviceManager--setOutputMode");
            AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.SpeakerPhone);
            if (mAudioManager.isWiredHeadsetOn()){
                mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                // mAudioManager.setSpeakerphoneOn(false);//关闭扬声器
                AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.Handset);
            }

            mSession.publish(mPublisher);
        }
    }

    /**
     * Called after a connection to the server hosting the OpenTok session has been created.
     * Alerts the Main Activity that the session was connected
     *
     * @param session    the session which was connected to
     * @param connection the connection which was created
     */
    @Override
    public void onConnectionCreated(Session session, Connection connection) {
        Log.i("yuyong_video", "onConnectionCreated: ");
        MyLog.writeLog("OkHttp", "onConnectionCreated=================================== ");
        Log.i("yuyong_video_call", "onConnectionCreated");
        showMainVideoScreen();
    }

    /**
     * Called when the connection to the OpenTok Server is destroyed.
     * Alerts the activity that the connection was destroyed.
     *
     * @param session    the session who's connection was destroyed
     * @param connection the connection which was destroyed
     */
    @Override
    public void onConnectionDestroyed(Session session, Connection connection) {
        Log.i("yuyong_video", "onConnectionDestroyed: ");
        Log.i("yuyong_video_call", "onConnectionDestroyed");
        MyLog.writeLog("OkHttp", "onConnectionDestroyed=================================== ");
        if (chatInterface != null) chatInterface.finishVideo(false);
    }

    /**
     * Called when the Session is disconnected.
     * clears the video stream views and sets up the streams for garbage collection.
     *
     * @param session the session to disconnect from
     */
    @Override
    public void onDisconnected(Session session) {
        Log.i("yuyong_video", "onDisconnected: ");
        MyLog.writeLog("OkHttp", "onDisconnected=================================== ");
        if (mPublisher != null) publisherView.removeView(mPublisher.getView());

        if (mSubscriber != null) subscriberView.removeView(mSubscriber.getView());

        mPublisher = null;
        mSubscriber = null;
        mSession = null;
    }

    /**
     * Attempts to setup a subscriber from a stream.
     *
     * @param stream the stream to subscribe to
     */
    private void subscribeToStream(Stream stream) {
        Log.i("yuyong_video", "subscribeToStream: ");
        MyLog.writeLog("OkHttp", "subscribeToStream=================================== ");
        mSubscriber = new Subscriber(getActivity(), stream);
        mSubscriber.setVideoListener(this);
        mSession.subscribe(mSubscriber);
    }

    /**
     * Attempts to end a subscription to a stream. Clears the subscriber view and alerts
     * the calling activity that the stream was de-subscribed.
     *
     * @param stream the stream to un-subscribe from
     */
    private void unSubscribeFromStream(Stream stream) {
        Log.i("yuyong_video", "unSubscribeFromStream: ");
        MyLog.writeLog("OkHttp", "unSubscribeFromStream=================================== ");
        boolean subChecks = mSubscriber != null && mSubscriber.getStream() != null;
        if (subChecks && stream != null && mSubscriber.getStream().equals(stream)) {
            subscriberView.removeView(mSubscriber.getView());
            mSubscriber = null;
        }

        if (chatInterface != null) chatInterface.finishVideo(false);
    }

    /**
     * Attaches a subscriber to it's corresponding view on the screen.
     *
     * @param subscriber the subscriber to attach to the view
     */
    private void attachSubscriberView(Subscriber subscriber) {
        Log.i("yuyong_video", "attachSubscriberView: ");
        MyLog.writeLog("OkHttp", "attachSubscriberView=================================== ");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels, getResources()
                .getDisplayMetrics().heightPixels);
        subscriberView.removeView(mSubscriber.getView());
        subscriberView.addView(subscriber.getView(), layoutParams);
        subscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
    }

    /**
     * Attaches a the user's publisher to the corresponding view on the screen.
     */
    private void attachPublisherView() {
        Log.i("yuyong_video", "attachPublisherView: ");
        MyLog.writeLog("OkHttp", "attachPublisherView=================================== ");
        mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        publisherView.addView(mPublisher.getView());
    }

    @Override
    public void onError(Session session, OpentokError exception) {
        Log.i("yuyong_video", "onError: " + exception.getMessage());
        MyLog.writeLog("OkHttp", "onError=================================== ");
        Log.i(TAG, "Session exception: " + exception.getMessage());
        Log.i("yuyong_video_call", "Error--" + session.getSessionId() + "--" + exception.getMessage());
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i("yuyong_video", "onStreamReceived: ");
        MyLog.writeLog("OkHttp", "onStreamReceived=================================== ");
        subscribeToStream(stream);
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i("yuyong_video", "onStreamDropped: ");
        MyLog.writeLog("OkHttp", "onStreamDropped=================================== ");
        if (mSubscriber != null) unSubscribeFromStream(stream);
    }

    @Override
    public void onStreamCreated(PublisherKit publisher, Stream stream) {
        Log.i("yuyong_video", "onStreamCreated: ");
        MyLog.writeLog("OkHttp", "onStreamCreated=================================== ");

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisher, Stream stream) {
        Log.i("yuyong_video", "onStreamDestroyed: ");
        MyLog.writeLog("OkHttp", "onStreamDestroyed=================================== ");
        mSession.unpublish(publisher);
        unSubscribeFromStream(stream);
    }

    @Override
    public void onError(PublisherKit publisher, OpentokError exception) {
        Log.i("yuyong_video", "onError: " + exception.getMessage());
        MyLog.writeLog("OkHttp", "onError=================================== ");
        Log.i(TAG, "Publisher exception: " + exception.getMessage());
    }

    @Override
    public void onVideoDataReceived(SubscriberKit subscriber) {
        Log.i("yuyong_video", "onVideoDataReceived: ");
        MyLog.writeLog("OkHttp", "onVideoDataReceived=================================== ");
        // stop loading spinning
        progressBar.setVisibility(View.GONE);
        attachSubscriberView(mSubscriber);
    }

    @Override
    public void onVideoDisabled(SubscriberKit subscriber, String reason) {
        Log.i("yuyong_video", "onVideoDisabled: ");
        MyLog.writeLog("OkHttp", "onVideoDataReceived=================================== ");
        mainOffIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVideoEnabled(SubscriberKit subscriber, String reason) {
        Log.i("yuyong_video", "onVideoEnabled: ");
        MyLog.writeLog("OkHttp", "onVideoEnabled=================================== ");
        publisherView.setVisibility(View.VISIBLE);
        reloadInterface();
    }

    @Override
    public void onVideoDisableWarning(SubscriberKit subscriber) {
        Log.i("yuyong_video", "onVideoDisableWarning: ");
        MyLog.writeLog("OkHttp", "onVideoDisableWarning=================================== ");
        Log.i(TAG, "Video may be disabled soon due to network quality degradation. Add UI handling here.");
    }

    @Override
    public void onVideoDisableWarningLifted(SubscriberKit subscriber) {
        Log.i("yuyong_video", "onVideoDisableWarningLifted: ");
        MyLog.writeLog("OkHttp", "onVideoDisableWarning=================================== ");
        Log.i(TAG, "Video may no longer be disabled as stream quality improved. Add UI handling here.");
    }
}
