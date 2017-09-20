package com.hsdi.NetMe.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hsdi.NetMe.util.PreferenceManager;

import java.io.IOException;

/**
 * Registers the application with GCM servers asynchronously.
 * <p>
 * Stores the registration ID and app versionCode in the application's
 * shared preferences.
 */
public class GCMRegisterTask extends AsyncTask<Void,Void,Boolean> {
    private static final String TAG = "RegisterInBack";
    private static final String SENDER_ID = "123880265184";

    private final Context context;
    private final Callback callback;

    public GCMRegisterTask(Context context, Callback callback) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        GoogleCloudMessaging gcm = null;
        try {
            // try to get gcm id
            gcm = GoogleCloudMessaging.getInstance(context);
            String regId = gcm.register(SENDER_ID);

            // register fine, storing id
            if(!regId.isEmpty()) {
                //save id
                PreferenceManager.getInstance(context).storeGCMRegistrationId(regId);
                return true;
            }
        }
        // failed to get gcm id
        catch (IOException ex) {
            gcm.close();
            Log.e(TAG, "Error registering with gcm: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        //report back with success or error
        callback.onComplete(bool);
    }

    public interface Callback{
        void onComplete(Boolean result);
    }
}
