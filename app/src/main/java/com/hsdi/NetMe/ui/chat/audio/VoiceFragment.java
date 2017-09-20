package com.hsdi.NetMe.ui.chat.audio;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.hsdi.NetMe.ui.chat.ChatInterface;
import com.hsdi.NetMe.ui.chat.video.AnswerFragment;
import com.hsdi.NetMe.ui.chat.video.CallFragment;
import com.hsdi.NetMe.ui.chat.video.ConnectingFragment;
import com.hsdi.NetMe.ui.chat.video.VideoInterface;
import com.hsdi.NetMe.util.Constants;
import com.hsdi.NetMe.util.MyLog;
import com.hsdi.NetMe.util.PreferenceManager;
import com.opentok.android.AudioDeviceManager;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.squareup.picasso.Picasso;

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
 * Created by huohong.yi on 2017/6/16.
 */

public class VoiceFragment extends Fragment implements VideoInterface, Session.SessionListener, Session.ConnectionListener, PublisherKit.PublisherListener, SubscriberKit.VideoListener {

    private static final int REQUEST_PERMISSIONS = 5555;


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

    private AudioManager mAudioManager;
    private SensorManager mSensorManager;
    private Sensor mProximiny;
    private SensorEventListener mEventListener;
    private float f_proximiny; // 当前传感器距离

    @Bind(R.id.voice_mute)
    ImageView voiceMute;
    @Bind(R.id.voice_end)
    ImageView voiceEnd;
    @Bind(R.id.voice_show_chat)
    ImageView voiceShowChat;
    @Bind(R.id.voice_controls_wrapper)
    LinearLayout voiceControlsWrapper;
    @Bind(R.id.voice_name)
    TextView voiceName;
    @Bind(R.id.voice_time)
    TextView voiceTime;
    @Bind(R.id.voice_avatar)
    ImageView voiceAvatar;
    @Bind(R.id.voice_progressbar)
    ProgressBar voiceProgressbar;

    @Bind(R.id.voice_fragment_holder)
    FrameLayout voiceFragmentHolder;

    public VoiceFragment() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_voice, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        Log.i("yuyong_video_call", "onCreateView：AudioDeviceManager--setOutputMode " + mAudioManager.isWiredHeadsetOn());
        if (mAudioManager.isWiredHeadsetOn()){
            mAudioManager.setSpeakerphoneOn(false);//关闭扬声器
            // mAudioManager.setMode(AudioManager.MODE_RINGTONE);
        }

        intiSensor();
       // audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        if (checkPermissions()) initialize();
        else requestVideoPermissions();
        return view;
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
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onPause: ");
        // pauses the voice session if there is one
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
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onResume: ");
        // resume the video session if there was one
        if (mSession != null) mSession.onResume();

        // if the timer was already started, continue it. Ignore otherwise
        if (startTime > -1) startTimeCounter();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onRequestPermissionsResult: ");
        if (checkPermissions()) initialize();
        else if (chatInterface != null) chatInterface.finishVideo(false);
        else getActivity().finish();
    }

    private void loadAvatar() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:loadAvatar: ");
        String number = getArguments().getString(Constants.EXTRA_INVITED);
        if (number != null) {
            Contact contact = NetMeApp.getContactWith(number);
            if (contact != null) {
                voiceName.setText(contact.getName());
                if (contact.getAvatarUri() != null) {
                    Picasso.with(getActivity())
                            .load(contact.getAvatarUri())
                            .into(voiceAvatar);
                }
            } else {
                voiceName.setText(number);
            }
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
                  //  mAudioManager.setMode(AudioManager.MODE_NORMAL);
                    mAudioManager.setSpeakerphoneOn(true);
                    Log.i("yuyong_sensor", "onSensorChanged: MODE_NORMAL");
                } else {
                    // setInCallBySdk();//稍后会展示这个方法
                    mAudioManager.setSpeakerphoneOn(false);//关闭扬声器
                    mAudioManager.setRouting(AudioManager.MODE_IN_COMMUNICATION, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);

                    if (getActivity()!= null) {
                        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    }
                   // mAudioManager.setMode(AudioManager.MODE_IN_CALL);
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
     * Sets up the video call screens depending on the type of call
     */
    private void initialize() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:initialize: ");
        Bundle extras = getArguments();
        switch (extras.getInt(Constants.EXTRA_TYPE, -1)) {
            // The user is calling someone
            case Constants.TYPE_START:
                Log.i(Constants.TAG_VOICE, "VoiceFragment:TYPE_START: ");
                invited = extras.getString(Constants.EXTRA_INVITED);
                long chatId = extras.getLong(Constants.EXTRA_CHAT_ID, -1);
                showCallingScreen();
                startVoice(chatId);
                break;

            // Someone is calling the user, get the info and alert the user
            case Constants.TYPE_RING:
                Log.i(Constants.TAG_VOICE, "VoiceFragment:TYPE_RING: ");
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
                Log.i(Constants.TAG_VOICE, "VoiceFragment:TYPE_SWITCH: ");
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

    /**
     * Creates a timer which will update the elapsed time of the session at the top of the screen
     */
    private void startTimeCounter() {
        Log.i(Constants.TAG_VOICE, "startTimeCounter: ");
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
                                    Log.i(Constants.TAG_VOICE, "schedule: formattedTime = " + formattedTime);
                                    if (voiceTime == null) {
                                        Log.i(Constants.TAG_VOICE, "voiceTime == null");
                                        return;
                                    }
                                    voiceTime.setText(formattedTime);
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
        Log.i(Constants.TAG_VOICE, "getMeetingId: ");
        return meetingId;
    }

    //--------------------------------------------------------------------------------------------------


    /**
     * Returns if permissions to access/use the  mic have been granted.
     *
     * @return {@code TRUE} is both have been granted, {@code FALSE} otherwise
     */
    private boolean checkPermissions() {
        int micPStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        return micPStatus == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests that the user grant permission to access/use the microphone.
     * Will only request permission which have not been granted.
     */
    private void requestVideoPermissions() {
        FragmentCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_PERMISSIONS
        );
    }

    //--------------------------------------------------------------------------------------------------


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    /** EventBus listener for when the invited user decided to accept/join the meeting */
    public void onMeetingUpdate(EventMeetingUpdate eventMeetingUpdate) {
        Log.i(Constants.TAG_VOICE, "onMeetingUpdate: ");
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


    /**
     * changes the fragment in front of all others to be the answering fragment.
     *
     * @param number the number to show on the answer screen
     * @param name   the name to show on the answer screen
     */
    public void showAnswerScreen(String number, String name) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:showAnswerScreen: ");
        Bundle answerExtras = new Bundle();
        answerExtras.putString(AnswerFragment.EXTRA_NUMBER, number);
        answerExtras.putString(AnswerFragment.EXTRA_NAME, name);
        fragment = new AnswerFragment();
        fragment.setArguments(answerExtras);
        getChildFragmentManager().beginTransaction()
                .add(R.id.voice_fragment_holder, fragment)
                .commitAllowingStateLoss();

    }

    /**
     * Changes the fragment in front of all others to be the calling fragment
     */
    public void showCallingScreen() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:showCallingScreen: ");
        Bundle callExtras = new Bundle();
        callExtras.putString(CallFragment.EXTRA_NUMBER, invited);
        fragment = new CallFragment();
        fragment.setArguments(callExtras);
        getChildFragmentManager().beginTransaction()
                .add(R.id.voice_fragment_holder, fragment)
                .commitAllowingStateLoss();
    }

    public void showConnectingScreen() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:showConnectingScreen: ");
        Bundle connectingExtras = new Bundle();
        connectingExtras.putString(ConnectingFragment.EXTRA_NUMBER, invited);
        fragment = new ConnectingFragment();
        fragment.setArguments(connectingExtras);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.voice_fragment_holder, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * Removes all other fragments in front to only show the main video content and controls
     */
    public void showMainVideoScreen() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:showMainVideoScreen: ");
        loadAvatar();
        getChildFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
        startTimeCounter();
        voiceFragmentHolder.setVisibility(View.GONE);
    }

    @OnClick(R.id.voice_end)
    protected void endVoiceChat(View view) {
        Log.i(Constants.TAG_VOICE, "endVoiceChat: ");
        chatInterface.finishVideo(true);
    }

    /**
     * Toggles the microphone's current streaming between it's on/off states
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.voice_mute)
    protected void toggleMicrophone(View view) {
        Log.i(Constants.TAG_VOICE, "toggleMicrophone: ");
        boolean result = true;

        // toggles whether the publisher streams out any audio
        if (mPublisher != null) {
            result = !mPublisher.getPublishAudio();
            mPublisher.setPublishAudio(result);
        }

        // changes the image of the mute button to represent the state
        if (result) voiceMute.setImageResource(R.drawable.voice_button);
        else voiceMute.setImageResource(R.drawable.voice_select_button);
    }

    private boolean selected = false;

    @SuppressWarnings("unused")
    @OnClick(R.id.voice_show_handsfree)
    protected void setSpeakerphoneOn(View view) {
        Log.i(Constants.TAG_VOICE, "setSpeakerphoneOn: " + selected);

      /*  if (mPublisher != null) {
            // changes the image of the mute button to represent the state
            if (selected) {
                voiceShowHandsfree.setImageResource(R.drawable.hands_free_select);
                audioManager.setSpeakerphoneOn(true);
            } else {
                voiceShowHandsfree.setImageResource(R.drawable.hands_free);
                audioManager.setSpeakerphoneOn(false);//关闭扬声器
                audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
                getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                //把声音设定成Earpiece（听筒）出来，设定为正在通话中
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
            //setSpeakerphoneOn(selected);
            selected = !selected;
        }*/

    }

    @SuppressWarnings("unused")
    @OnClick(R.id.voice_show_chat)
    protected void showTextChat(View view) {
        Log.i(Constants.TAG_VOICE, "showTextChat: ");
        chatInterface.toggleFullScreen(false, Constants.MEETING_VOICE);
    }

    /**
     * Hides the voice controls to simulate a switch to full screen.
     */
    public void hideScreenControls() {
        Log.i(Constants.TAG_VOICE, "hideScreenControls: ");
        voiceControlsWrapper.setVisibility(View.GONE);
    }

    /**
     * Shows the voice controls
     */
    public void showScreenControls() {
        Log.i(Constants.TAG_VOICE, "showScreenControls: ");
        voiceControlsWrapper.setVisibility(View.VISIBLE);
    }


    /**
     * Runs the api command to start a new voice meeting with the invited user.
     * Attempts to find and continue the most recent text chat, otherwise waits for the back end
     * to send it a new one.
     */
    private void startVoice(long chatId) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment: startVoice = " + chatId);
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
                        Constants.MEETING_VOICE
                )
                .enqueue(new Callback<StartMeetingResponse>() {
                    @Override
                    public void onResponse(Call<StartMeetingResponse> call, Response<StartMeetingResponse> response) {

                        MyLog.writeLog("OkHttp", "startVideo:onResponse===================================" + response.message() + "*****");
                        Log.i(Constants.TAG_VOICE, "startVideo:onResponse===================================" + response.message() + "*****");
                        if (response != null && response.body() != null) {
                            StartMeetingResponse smr = response.body();
                            // get the params for the meeting
                            meetingId = smr.getMeetingId();
                            sessionId = smr.getSessionId();
                            token = smr.getToken();
                            MyLog.writeLog("OkHttp", "startVideo:onResponse=================================== meetingId = " + meetingId + ",token = " + token);
                            Log.i(Constants.TAG_VOICE, "startVideo:onResponse=================================== meetingId = " + meetingId + ",token = " + token);
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
        Log.i(Constants.TAG_VOICE, "VoiceFragment:sessionConnect: ");
        if (mSession == null) {
            MyLog.writeLog("OkHttp", "sessionConnect=================================== ");
            mSession = new Session(getActivity(), API_KEY, sessionId);
            mSession.setSessionListener(this);
            mSession.setConnectionListener(this);
            mSession.connect(token);
            MyLog.writeLog("OkHttp", "sessionConnect=================================== API_KEY = " + API_KEY + ",sessionId = " + sessionId);
            Log.i(Constants.TAG_VOICE, String.format("sessionConnect -- %s -- %s", sessionId, token));
        }
        // alert the history fragment to update
        EventBus.getDefault().post(new EventMeetingLog());

    }

    /**
     * Tells the OpenTok Session to disconnect and end
     */
    public void sessionDisconnect() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:sessionDisconnect: ");
        if (mSession != null) mSession.disconnect();
    }

    /**
     * Attempts to setup a subscriber from a stream.
     *
     * @param stream the stream to subscribe to
     */
    private void subscribeToStream(Stream stream) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:subscribeToStream: ");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber(getActivity(), stream);
            //只订阅音频，不订阅视频
            mSubscriber.setSubscribeToVideo(false);
            mSubscriber.setSubscribeToAudio(true);

            mSubscriber.setVideoListener(this);
            mSession.subscribe(mSubscriber);

        }
    }

    /**
     * Attempts to end a subscription to a stream. Clears the subscriber view and alerts
     * the calling activity that the stream was de-subscribed.
     *
     * @param stream the stream to un-subscribe from
     */
    private void unSubscribeFromStream(Stream stream) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:unSubscribeFromStream: ");
        boolean subChecks = mSubscriber != null && mSubscriber.getStream() != null;
        if (subChecks && stream != null && mSubscriber.getStream().equals(stream)) {
            mSubscriber = null;
        }
        if (chatInterface != null) chatInterface.finishVideo(false);
    }


    /**
     * Requests the session information to join the voice with the matching meetingId class variable.
     */
    @Override
    public void answerCall() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:answerCall: ");
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


    @Override
    public void denyCall() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:denyCall: ");
        Intent intent = new Intent(getActivity(), EndMeetingService.class);
        intent.putExtra(EndMeetingService.EXTRA_SERVICE_CMD, EndMeetingService.CMD_REJECT_VIDEO);
        intent.putExtra(EndMeetingService.EXTRA_MEETING_ID, meetingId);
        getActivity().startService(intent);

        chatInterface.finishVideo(false);
    }

    @Override
    public void cancelCall() {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:cancelCall: ");
        chatInterface.finishVideo(true);
    }

    @Override
    public void sendNotification() {

    }

    /**
     * implements PublisherListener (3 methods)
     *
     * @param publisherKit
     * @param stream
     */
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onStreamCreated: ");

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onStreamDestroyed: ");
        mSession.unpublish(publisherKit);
        unSubscribeFromStream(stream);

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onError: " + opentokError.getMessage());
    }


    @Override
    public void onConnectionCreated(Session session, Connection connection) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onConnectionCreated: ");
        showMainVideoScreen();
        // stop loading spinning
        voiceProgressbar.setVisibility(View.GONE);

    }

    @Override
    public void onConnectionDestroyed(Session session, Connection connection) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onConnectionDestroyed: ");
        if (chatInterface != null) chatInterface.finishVideo(false);
    }

    /**
     * Called after this user has been connected to the OpenTok Session.
     * A Publisher stream is then created and attached to it's corresponding view.
     *
     * @param session the session which was connected
     */
    @Override
    public void onConnected(Session session) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onConnected: ");
        if (mPublisher == null) {
            //mPublisher = new Publisher(getActivity(), NetMeApp.getCurrentUser());
            //Publisher(Context context, String name, boolean audioTrack, boolean videoTrack)
            mPublisher = new Publisher(getActivity(), NetMeApp.getCurrentUser(), true, false);
            mPublisher.setPublisherListener(this);

            //设置只发布声音
            mPublisher.setPublishVideo(false);
            mPublisher.setPublishAudio(true);

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
     * Called when the Session is disconnected.
     * clears the video stream views and sets up the streams for garbage collection.
     *
     * @param session the session to disconnect from
     */
    @Override
    public void onDisconnected(Session session) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onDisconnected: ");
        mPublisher = null;
        mSubscriber = null;
        mSession = null;
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onStreamReceived: ");
        subscribeToStream(stream);
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onStreamDropped: ");
        if (mSubscriber != null) unSubscribeFromStream(stream);
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(Constants.TAG_VOICE, "VoiceFragment:onError: " + opentokError.getMessage());
    }

    @Override
    public void onVideoDataReceived(SubscriberKit subscriberKit) {

    }

    @Override
    public void onVideoDisabled(SubscriberKit subscriberKit, String s) {

    }

    @Override
    public void onVideoEnabled(SubscriberKit subscriberKit, String s) {

    }

    @Override
    public void onVideoDisableWarning(SubscriberKit subscriberKit) {

    }

    @Override
    public void onVideoDisableWarningLifted(SubscriberKit subscriberKit) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
