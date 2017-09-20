package com.hsdi.NetMe.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by huohong.yi on 2017/8/11.
 */

public class HeadsetPlugReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (intent.hasExtra("state")){
            if (intent.getIntExtra("state", 0) == 0){
                Log.i("huohong_sensor", "onReceive:headset not connected : setSpeakerphoneOn(true)");
                audioManager.setSpeakerphoneOn(true);
            }
            else if (intent.getIntExtra("state", 0) == 1){
                Log.i("huohong_sensor", "onReceive:headset connected : setSpeakerphoneOn(false)");
                audioManager.setSpeakerphoneOn(false);
            }
        }
    }
}
