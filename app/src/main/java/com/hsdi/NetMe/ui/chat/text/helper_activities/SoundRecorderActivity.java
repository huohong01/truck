package com.hsdi.NetMe.ui.chat.text.helper_activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hsdi.NetMe.BaseActivity;
import com.hsdi.NetMe.R;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hsdi.NetMe.R.drawable.pause_btn;

/**
 * Created by Daniel.DeLeon on 11/23/2015.
 *
 * Records audio from the device's microphone, stores the file, and returns the full file path
 *  in the result
 */
public class SoundRecorderActivity extends BaseActivity {
    private static final String TAG = "AudioRecordTest";
    private static final int REQUEST_CODE_PERMISSION = 44;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({INITIAL, RECORDING, PLAYING, WAITING})
    private @interface STATE{}

    private static final int INITIAL    = 0;
    private static final int RECORDING  = 1;
    private static final int PLAYING    = 2;
    private static final int WAITING    = 3;

    @SoundRecorderActivity.STATE
    private int currentState = INITIAL;

    private static final String STATE_TITLE_INITIAL = "Click to record";
    private static final String STATE_TITLE_RECORDING = "Recording";
    private static final String STATE_TITLE_PLAYING = "Playing";
    private static final String STATE_TITLE_WAITING = "Click to play";

    private static File soundFile = null;
    private int maxDuration = 0;
    private long maxSize = 0;
    private int recordLength = 0;
    private int playTime = 0;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private Timer recordingTimer = null;
    private Timer playingTimer = null;

    @Bind(R.id.sound_recorder_toolbar)Toolbar toolbar;
    @Bind(R.id.sound_recorder_cancel)View cancelBtn;
    @Bind(R.id.sound_recorder_accept)View acceptBtn;
    @Bind(R.id.sound_recorder_state)TextView stateText;
    @Bind(R.id.sound_recorder_time)TextView time;
    @Bind(R.id.sound_recorder_button)ImageView actionButton;
    @Bind(R.id.sound_recorder_seekbar)SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_recorder);
        ButterKnife.bind(this);

        //setting up the toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stateText.setText(STATE_TITLE_INITIAL);

        //setting any parameters that where passed
        String output = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
        if(output != null && !output.isEmpty()) soundFile = new File(output);
        maxDuration = getIntent().getIntExtra(MediaStore.EXTRA_DURATION_LIMIT, 0);
        maxSize = getIntent().getLongExtra(MediaStore.EXTRA_SIZE_LIMIT, 0);

        //default result which gets overridden after a recording is stopped
        setResult(RESULT_CANCELED, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    SoundRecorderActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_CODE_PERMISSION
            );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null || mPlayer != null) onButtonClicked(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode != REQUEST_CODE_PERMISSION || grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onCancelClicked(null);
        super.onBackPressed();
    }

    public void onButtonClicked(View v){
        switch (currentState){
            case INITIAL:
                startRecording();
                stateText.setText(STATE_TITLE_RECORDING);
                actionButton.setImageResource(R.drawable.stop_rec);
                applyTheme(actionButton,R.drawable.stop_rec);

                currentState = RECORDING;
                break;
            case RECORDING:
                try {
                    stopRecording();
                    stateText.setText(STATE_TITLE_WAITING);
                    toolbar.setVisibility(View.INVISIBLE);
                    cancelBtn.setVisibility(View.VISIBLE);
                    acceptBtn.setVisibility(View.VISIBLE);
                    actionButton.setImageResource(R.drawable.play_btn);
                    applyTheme(actionButton,R.drawable.play_btn);
                    currentState = WAITING;
                }
                catch (Exception e) {
                    Log.d(TAG, "Failed to stop the recording", e);
                }
                break;
            case WAITING:
                startPlaying();
                stateText.setText(STATE_TITLE_PLAYING);
                actionButton.setImageResource(R.drawable.pause_btn);
                applyTheme(actionButton, pause_btn);
                currentState = PLAYING;
                break;
            case PLAYING:
                stopPlaying();
                stateText.setText(STATE_TITLE_WAITING);
                actionButton.setImageResource(R.drawable.play_btn);
                applyTheme(actionButton,R.drawable.play_btn);
                currentState = WAITING;
                break;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onCancelClicked(View v){
        //if currently playing audio
        if(currentState == PLAYING) onButtonClicked(null);

        //removing the current file
        String path = soundFile.getPath();
        soundFile.delete();
        soundFile = new File(path);

        //returning everything back to their initial states
        toolbar.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.INVISIBLE);
        acceptBtn.setVisibility(View.INVISIBLE);
        stateText.setText(STATE_TITLE_INITIAL);
        time.setText("");
        actionButton.setImageResource(R.drawable.start_rec);
        applyTheme(actionButton,R.drawable.start_rec);
        seekBar.setVisibility(View.INVISIBLE);
        currentState = INITIAL;
        setResult(RESULT_CANCELED, null);
    }

    public void onAcceptClicked(View v){
        finish();
    }

    private void createSaveFile() {
        String mFileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        Toast.makeText(this, mFileName, Toast.LENGTH_LONG).show();
        File voiceDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        soundFile = new File(voiceDir, mFileName + ".voice.3gp");
    }

    private void startRecording() {
        //in the case a file uri wasn't passed in, create a file
        if(soundFile == null) createSaveFile();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(soundFile.getPath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        if(maxDuration != 0) mRecorder.setMaxDuration(maxDuration);

        if(maxSize != 0) mRecorder.setMaxFileSize(maxSize);

        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                switch (what) {
                    case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                        onButtonClicked(null);
                        break;
                    case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                        onButtonClicked(null);
                        break;
                }
            }
        });

        try {
            mRecorder.prepare();

            recordLength = 0;
            recordingTimer = new Timer();
            recordingTimer.schedule(new RecordingTimerTask(),0,1000);

            mRecorder.start();
        }
        catch (IOException e){
            e.printStackTrace();
            Log.e(TAG, "prepare() failed");
            Toast.makeText(this, R.string.error_start_record, Toast.LENGTH_SHORT).show();
            onCancelClicked(null);
        }
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        if(recordingTimer != null) recordingTimer.cancel();

        Intent resultIntent = new Intent();
        resultIntent.setData(Uri.fromFile(soundFile));
        setResult(RESULT_OK, resultIntent);
    }

    private void startPlaying() {
        if(mPlayer != null) mPlayer.release();

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(soundFile.getPath());
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onButtonClicked(null);
                }
            });

            playTime = 0;
            setSeekBar();
            playingTimer = new Timer();
            playingTimer.schedule(new PlayerTimerTask(), 0, 100);

            mPlayer.start();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "prepare() failed");
            Toast.makeText(this, R.string.error_play_back, Toast.LENGTH_SHORT).show();
            onButtonClicked(null);
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;

        unSetSeekBar();
    }

    private void unSetSeekBar(){
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setProgress(0);

        if(playingTimer != null) playingTimer.cancel();

        time.setText(getFormattedTime(recordLength));
    }

    private void setSeekBar(){
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setMax(mPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null) {
                    mPlayer.seekTo(progress);
                    playTime = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String getFormattedTime(int totalSeconds){
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public class RecordingTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    time.setText(getFormattedTime(recordLength));
                    recordLength++;
                }
            });
        }
    }

    public class PlayerTimerTask extends TimerTask {
        @Override
        public void run() {
            //*NOTE: Unable to use the mPlayer.getCurrentPosition method due to it causing a stutter
            //  every time it is called
            final String formattedTime = getFormattedTime(playTime / 1000);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    time.setText(formattedTime);
                    seekBar.setProgress(playTime);
                    playTime += 100;
                }
            });
        }
    }
}