package com.hsdi.NetMe.ui.chat.video;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.util.DeviceUtils;
import com.hsdi.NetMe.util.MyLog;
import com.hsdi.NetMe.util.PhoneUtils;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * shows a answer/deny screen for incoming calls
 * */
public class AnswerFragment extends Fragment {
    private static final String TAG             = "";

    public static final String EXTRA_NUMBER     = "invited";
    public static final String EXTRA_NAME       = "name";
    private static final long TIME_OUT          = 60000;

    private VideoInterface videoInterface       = null;
    private Vibrator vibrator                   = null;
    private MediaPlayer mediaPlayer             = null;
    private Timer endTimer                      = null;

    @Bind(R.id.answer_image)                    ImageView imageView;
    @Bind(R.id.answer_name)                     TextView nameView;
    @Bind(R.id.answer_number)                   TextView numberView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("yuyong_video", "AnswerFragment:onCreateView: ");
        // Inflate the layout for this fragment
        View main = inflater.inflate(R.layout.fragment_answer, container, false);
        ButterKnife.bind(this, main);

        // setup the screen with it
        setupGeneralWaitScreen();

        // wake phone, sound, and vibrate to notify the user of incoming call
        DeviceUtils.unlockScreen(getActivity());
        mediaPlayer = DeviceUtils.playNotificationSound(getActivity());
        vibrator = DeviceUtils.startNotificationVibrations(getActivity());

        startEndTimer();
        return main;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {videoInterface = (VideoInterface) getParentFragment();}
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ChatInterface");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {videoInterface = (VideoInterface) getParentFragment();}
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChatInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        videoInterface = null;
        // stop the automatic reject timer
        if(endTimer != null) {
            endTimer.cancel();
            endTimer = null;
        }
        stopRing();
    }

    private void startEndTimer() {
        Log.i("yuyong_video", "AnswerFragment:startEndTimer: ");
        //start the timeout
        endTimer = new Timer();
        endTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if(getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyLog.writeLog("OkHttp","startEndTimer===================================");
                                    onDenyClicked(null);
                                    //videoInterface.sendNotification();
                                }
                            });
                        }
                    }
                },
                TIME_OUT
        );
    }

    /**
     * Sets up the fragment to show the calling contact's information as well as answer and deny buttons
     * */
    public void setupGeneralWaitScreen(){
        Log.i("yuyong_video", "AnswerFragment:setupGeneralWaitScreen: ");
        MyLog.writeLog("OkHttp","setupGeneralWaitScreen===================================");
        String callerNumber = getArguments().getString(EXTRA_NUMBER);
        String callerName = getArguments().getString(EXTRA_NAME);
        Contact contact = NetMeApp.getContactWith(callerNumber);

        // try to set the caller's name from the contact, passed name, or just put 'unknown'
        if(contact != null ) {
            // setting the contact's name
            nameView.setText(contact.getName());

            //load the contacts image if they have one
            if (contact.getAvatarUri() != null) {
                Picasso.with(getActivity())
                        .load(contact.getAvatarUri())
                        .into(imageView);
            }
            else Log.d(TAG, "Failed to load the contact's avatar image");
        }
        else if(callerName != null && !callerName.isEmpty()) nameView.setText(callerName);
        else nameView.setText(R.string.unknown);

        // trying to set a formatted version of the number number
        String formattedNumber = PhoneUtils.formatPhoneNumber(getActivity(), callerNumber);
        if (formattedNumber != null) numberView.setText(formattedNumber);
        else if (callerNumber != null && !callerNumber.isEmpty()) numberView.setText(callerNumber);
        else numberView.setText(R.string.unknown);
    }

    /**
     * Click method for the answer button. Starts the process of accepting and connecting the video.
     * @param view    the view that was clicked
     * */
    @SuppressWarnings("unused")
    @OnClick(R.id.answer_accept)
    protected void onAnswerClicked(View view) {
        Log.i("yuyong_video", "AnswerFragment:onAnswerClicked: ");
        MyLog.writeLog("OkHttp","onAnswerClicked===================================");
        if(endTimer != null) endTimer.cancel();
        if (videoInterface != null) videoInterface.answerCall();
    }

    /**
     * Click method for the decline button. Starts the process of declining the video call.
     * @param view    the view that was clicked
     * */
    @SuppressWarnings("unused")
    @OnClick(R.id.answer_decline)
    protected void onDenyClicked(View view) {
        Log.i("yuyong_video", "AnswerFragment:onDenyClicked: ");
        MyLog.writeLog("OkHttp","onDenyClicked===================================");
        if(endTimer != null) endTimer.cancel();
        if (videoInterface != null) videoInterface.denyCall();
    }

    private void stopRing() {
        Log.i("yuyong_video", "AnswerFragment:stopRing: ");
        // stop vibrations
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        // stop ring tone
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
}
