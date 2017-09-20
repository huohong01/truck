package com.opentok.android;

import android.content.Context;
import android.util.Log;

/**
 * Created by huohong.yi on 2017/4/17.
 */

public class LogSession extends Session {
    public LogSession(Context context, String apiKey, String sessionId) {
        super(context, apiKey, sessionId);
    }

    @Override
    public void connect(String token){
        AudioDeviceManager.defaultAudioDevice=new TestBaseAudioDevice();
        Log.i("yuyong_video_call","LogSession-->"+AudioDeviceManager.defaultAudioDevice.getClass().getName()+"-->"+DefaultAudioDevice.class.getName());
        super.connect(token);
    }

    private static class TestBaseAudioDevice extends BaseAudioDevice{
        @Override
        public boolean initCapturer() {
            return false;
        }

        @Override
        public boolean startCapturer() {
            return false;
        }

        @Override
        public boolean stopCapturer() {
            return false;
        }

        @Override
        public boolean destroyCapturer() {
            return false;
        }

        @Override
        public boolean initRenderer() {
            return false;
        }

        @Override
        public boolean startRenderer() {
            return false;
        }

        @Override
        public boolean stopRenderer() {
            return false;
        }

        @Override
        public boolean destroyRenderer() {
            return false;
        }

        @Override
        public int getEstimatedCaptureDelay() {
            return 0;
        }

        @Override
        public int getEstimatedRenderDelay() {
            return 0;
        }

        @Override
        public AudioSettings getCaptureSettings() {
            return null;
        }

        @Override
        public AudioSettings getRenderSettings() {
            return null;
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onResume() {

        }
    }
}
