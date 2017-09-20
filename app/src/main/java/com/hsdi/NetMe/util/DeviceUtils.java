package com.hsdi.NetMe.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * a collections of methods used for a variety of things.
 * Contains methods for:
 *      querying/editing user parameters and device parameters to the device's {@link SharedPreferences}
 *      checking the device's states and settings
 *      creating notifications and alerts
 *      saving and opening files
 * */
public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    /** Check if has internet connection */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /** Get unique deviceId  */
    public static String getDeviceId(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = telephonyManager.getDeviceId();

        if (id == null || id.isEmpty()) id = telephonyManager.getSubscriberId();
        if (id == null || id.isEmpty()) id = telephonyManager.getSimSerialNumber();
        if (id == null || id.isEmpty()) id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (id == null || id.isEmpty()) id = telephonyManager.getDeviceId();

        return id;
    }

    /** Get SDK version */
    public static String getPlatformVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /** Get app version. If fail, return "1.0" */
    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        }
        catch (Exception e) {e.printStackTrace();}

        return "1.0";
    }


//--------------------------------------------------------------------------------------------------


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getEncryptedMediaDirectory(Context context) {
        File dir = new File(context.getFilesDir().getAbsolutePath() + File.separator + "Media");
        dir.mkdirs();
        return dir;
    }

    public static File saveToDownloads(Context context, byte[] fileData, String fileName) throws IOException {
        // save a new file to downloads
        File fileInDownloads = saveToDirectory(
                fileData,
                fileName,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        );

        // get media scanner to scan this one file so it appears in the appropriate places
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(fileInDownloads);
        scanIntent.setData(contentUri);
        context.sendBroadcast(scanIntent);

        return fileInDownloads;
    }

    public static File saveToCache(Context contexts, byte[] fileData, String fileName, boolean replaceOld) throws IOException {
        File cacheDir = NetMeApp.getInstance().getCacheDir();
        File fileInCache = new File(cacheDir, fileName);

        // request to replace old, delete it if it exists
        if(replaceOld && fileInCache.exists()) {fileInCache.delete();}

        // save the file to the cache directory
        return saveToDirectory(
                fileData,
                fileName,
                cacheDir
        );
    }

    public static File saveToDirectory(byte[] fileData, String fileName, File directory) throws IOException {
        // create external file
        File outputFile;
        int fileNameIndexer = 1;
        String adjustedName = fileName;
        boolean exists;
        do {
            outputFile = new File(directory, adjustedName);

            // if the file exits, adjust the name for this file
            if(exists = outputFile.exists()) {
                // add a '(#)' to the file name before the file extension
                int fileExtensionIndex = fileName.lastIndexOf('.');
                adjustedName = fileName.substring(0, fileExtensionIndex) +
                        "(" + fileNameIndexer + ")" +
                        fileName.substring(fileExtensionIndex);
                fileNameIndexer++;
            }
        } while(exists);

        // write to external file
        FileOutputStream fileOut = new FileOutputStream(outputFile);
        BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
        bufferedOut.write(fileData);
        bufferedOut.flush();
        bufferedOut.close();

        Log.d(TAG, adjustedName + " successfully saved to Downloads");

        return outputFile;
    }

    public static boolean deleteEncryptedMediaFile(String fileName) {
        File mediaFile = new File(
                DeviceUtils.getEncryptedMediaDirectory(NetMeApp.getInstance()),
                fileName
        );

        return mediaFile.exists() && mediaFile.delete();
    }

    public static boolean requestFileExplorerAt(Context context, File directory) {
        Uri selectedUri = Uri.parse(directory.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");

        if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null) {
            context.startActivity(intent);
            return true;
        }

        return false;
    }

    public static void printCursor(Cursor cursor){
        int[] columnLength;

        if(cursor != null) {
            columnLength = new int[cursor.getColumnCount()];
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                columnLength[i] = 1;
            }

            if(cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        int length;

                        if (cursor.getString(i) != null) length = cursor.getString(i).length();
                        else length = "null".length();

                        if (length > columnLength[i]) columnLength[i] = length;
                    }
                } while (cursor.moveToNext());
            }

            if(cursor.moveToFirst()) {
                do {
                    StringBuilder output = new StringBuilder();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        output.append(cursor.getColumnName(i));
                        String format = ": %-" + columnLength[i] + "s  ";
                        output.append(String.format(format, cursor.getString(i)));
                    }
                    Log.d(TAG, output.toString());
                }while (cursor.moveToNext());
            }
        }
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Vibrates the phone for half a second
     * @param context context
     */
    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    /**
     * Unlocks and wake the screen up
     * @param activity activity
     */
    public static void unlockScreen(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    /**
     * determines and sets what to output the audio and what voume stream to connect it to
     * */
    public static void setAudioOutputDevice(Activity activity){
        //setting the apps audio levels to be controlled by the music levels
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        //this back and forth resets the audio stream and makes sure it is normal
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * checks if the phone's sound is on and lays the phone's standard ringtone, then returns the player
     * @param context context
     * @return the media player
     */
    public static MediaPlayer playNotificationSound(Context context) {
        MediaPlayer mMediaPlayer;
        try {
            Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            if(alert == null){throw new Exception("Couldn't find the default ringtone");}
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                return mMediaPlayer;
            }
        } catch(Exception e) {
            Log.e("DeviceUtil", "Error trying to play ringtone");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * vibrates the phone in an on...off pattern
     * @param context context
     * @return the vibrator class doing the vibrations
     * */
    public static Vibrator startNotificationVibrations(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        AudioManager AM = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        boolean silent = AM.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
        if(vibrator.hasVibrator() && !silent){
            long[] pattern = {0,1000,500};
            vibrator.vibrate(pattern, 0);
            return vibrator;
        }
        return null;
    }

    /**
     * plays dialing sound
     * @return the tone generator that will play the dial tone
     * */
    public static ToneGenerator playDialingSound(){
        ToneGenerator dialTone = new ToneGenerator(AudioManager.STREAM_DTMF, ToneGenerator.MAX_VOLUME);
        dialTone.startTone(ToneGenerator.TONE_SUP_RINGTONE);
        return dialTone;
    }


//--------------------------------- Alerts and Notifications ---------------------------------------


    /**
     * generates a normal/non-heads up/non-updating notification
     * @param context context
     * @param message the message to display
     * @param pendingIntent a pending intent for the notification to launch once clicked
     * */
    public static void generateNotification(Context context, String message, PendingIntent pendingIntent){
        int icon = R.mipmap.ic_notifications;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }
}
