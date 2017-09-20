package com.hsdi.NetMe.ui.chat.video;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
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
import com.hsdi.NetMe.util.PhoneUtils;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * shows a calling screen with end button when making a video call
 * */
public class CallFragment extends Fragment {
    private static final String TAG             = "CallFrag";

    public static final String EXTRA_NUMBER = "invite_number";
    private static final long TIME_OUT          = 60000;

    private VideoInterface videoInterface       = null;
    private Timer cancelTimer                   = null;
    private ToneGenerator dialTone              = null;
    private AudioManager mAudioManager;


    @Bind(R.id.call_image)                      ImageView imageView;
    @Bind(R.id.call_name)                       TextView nameView;
    @Bind(R.id.call_number)                     TextView numberView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("yuyong_video", "CallFragment:onCreateView: ");
        // Inflate the layout for this fragment
        View main = inflater.inflate(R.layout.fragment_call, container, false);
        ButterKnife.bind(this, main);
        setupGeneralWaitScreen();
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager.isWiredHeadsetOn()){
            mAudioManager.setSpeakerphoneOn(false);//关闭扬声器
            //mAudioManager.setMode(AudioManager.MODE_RINGTONE);
        }

        return main;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {videoInterface = (VideoInterface) getParentFragment();}
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement VideoFragmentListener");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {videoInterface = (VideoInterface) getParentFragment();}
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement VideoFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        videoInterface = null;

        // stop the automatic reject timer
        if(cancelTimer != null) {
            cancelTimer.cancel();
            cancelTimer = null;
        }

        // make sure to stop the calling sound
        if(dialTone != null) {
            dialTone.stopTone();
            dialTone = null;
        }
    }

    /** Starts the timer which will automatically stop the call if not answered by the other person. */
    public void startCancelTimer() {
        Log.i("yuyong_video", "CallFragment:startCancelTimer: ");
        cancelTimer = new Timer();
        cancelTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if(getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onEndClicked(null);
                                }
                            });
                        }
                    }
                },
                TIME_OUT
        );

        // make sure to also play the calling sound at this time too
        dialTone = DeviceUtils.playDialingSound();
    }

    /** sets up the calling screen with that contacts information and the end button */
    private void setupGeneralWaitScreen() {
        Log.i("yuyong_video", "CallFragment:setupGeneralWaitScreen: ");
        String invitedNumber = getArguments().getString(EXTRA_NUMBER);
        Contact contact = NetMeApp.getContactWith(invitedNumber);

        // setting the contact's name
        if(contact != null) nameView.setText(contact.getName());
        else nameView.setText(invitedNumber);

        // trying to set a formatted version of the number number
        String formattedNumber = PhoneUtils.formatPhoneNumber(getActivity(), invitedNumber);
        if(formattedNumber != null) numberView.setText(formattedNumber);
        else if(invitedNumber != null && !invitedNumber.isEmpty()) numberView.setText(invitedNumber);
        else numberView.setText(R.string.unknown);

        // setting the contacts avatar
        if(contact != null && contact.getAvatarUri() != null) {
            Picasso.with(getActivity())
                    .load(contact.getAvatarUri())
                    .into(imageView);
        }
        else Log.d(TAG, "Failed to load the contacts avatar");
    }

    /**
     * Called byt the end button, cancels the automatic canceling timer and tells the video fragment
     * that the end button was pressed
     * @param view    the view that was clicked
     * */
    @SuppressWarnings("unused")
    @OnClick(R.id.call_end_btn)
    protected void onEndClicked(View view) {
        Log.i("yuyong_video", "CallFragment:onEndClicked: ");
        if(cancelTimer != null) cancelTimer.cancel();
        if(videoInterface != null) videoInterface.cancelCall();
    }
}
